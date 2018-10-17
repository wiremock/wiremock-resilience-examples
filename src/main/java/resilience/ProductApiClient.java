package resilience;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
public class ProductApiClient {

    @Autowired
    private RestTemplate restTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Value("${productsapi.baseurl}")
    String baseUrl;

    public ProductDetails getProduct(String productId) {
        String productJson = restTemplate.getForObject(baseUrl + "/products/" + productId, String.class);
        try {
            return objectMapper.readValue(productJson, ProductDetails.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
