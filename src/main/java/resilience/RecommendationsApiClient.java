package resilience;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class RecommendationsApiClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${recommendationsapi.baseurl}")
    String baseUrl;

    @SuppressWarnings("unchecked")
    public List<RecommendedProduct> getRecommendations(String productId) {
        return restTemplate.getForObject(baseUrl + "/recommendations?productId=" + productId, List.class);
    }


}
