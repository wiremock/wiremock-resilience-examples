package resilience;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;

@Controller
public class ShopResource {

    private static final List<RecommendedProduct> DEFAULT_RECOMMENDATIONS = singletonList(
            new RecommendedProduct("Default Recommendation", "Boring but quick")
    );

    @Autowired
    private ProductApiClient productApiClient;

    @Autowired
    private RecommendationsApiClient recommendationsApiClient;

    @GetMapping("/products/{id}")
    public ModelAndView getProduct(@PathVariable("id") String productId) {
        ProductDetails productDetails = productApiClient.getProduct(productId);
        List<RecommendedProduct> recommendedations;
        try {
            recommendedations = recommendationsApiClient.getRecommendations(productId);
        } catch (ResourceAccessException e) {
            recommendedations = DEFAULT_RECOMMENDATIONS;
        }

        Map<String, Object> model = new HashMap<>();
        model.put("product", productDetails);
        model.put("recommendations", recommendedations);

        return new ModelAndView("product", model);
    }
}
