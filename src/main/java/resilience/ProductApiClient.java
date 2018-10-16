package resilience;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Component
public class ProductApiClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ProductApiClient() {
        httpClient = HttpClientBuilder.create().build();
        objectMapper = new ObjectMapper();
    }

    @Value("${productsapi.baseurl}")
    String baseUrl;

    public ProductDetails getProduct(String productId) {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(200))
                .build();
        Retry retry = Retry.of("retry-product", config);

        return retry.executeSupplier(() -> doGetProduct(productId));
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
