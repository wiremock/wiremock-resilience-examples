package resilience;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;

@Component
public class ProductApiClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final TimeLimiter timeLimiter;
    private final ExecutorService executor;

    public ProductApiClient() {
        httpClient = HttpClientBuilder.create().build();
        objectMapper = new ObjectMapper();

        TimeLimiterConfig config = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(1))
                .build();

        timeLimiter = TimeLimiter.of(config);
        executor = Executors.newFixedThreadPool(5);
    }

    @Value("${productsapi.baseurl}")
    String baseUrl;

    public ProductDetails getProduct(String productId) {
        return wrapTimelimit(productId);
    }

    private ProductDetails wrapTimelimit(String productId) {
        Supplier<Future<ProductDetails>> futureSupplier = () -> executor.submit(() -> doGetProduct(productId));
        try {
            return timeLimiter.executeFutureSupplier(futureSupplier);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ProductDetails doGetProduct(String productId) {
        try {
            String url = baseUrl + "/products/" + productId;
            HttpResponse httpResponse = httpClient.execute(new HttpGet(url));
            if (httpResponse.getStatusLine().getStatusCode() == 502) {
                System.err.println("Oops!"); // Don't try this at home. This is the worst possible way to handle a real error!
                return null;
            }
            return objectMapper.readValue(EntityUtils.toByteArray(httpResponse.getEntity()), ProductDetails.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
