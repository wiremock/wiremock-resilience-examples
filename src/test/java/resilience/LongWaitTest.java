package resilience;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LongWaitTest extends TestBase {

    @Test(timeout = 3000)
    public void returns_ok_with_default_recommendation_when_recommendations_api_response_is_delayed() {
        mockProductApi.stubFor(
                get(urlPathEqualTo("/api/products/123"))
                        .willReturn(ok()
                                .withHeader("Content-Type", "application/json")
                                .withBodyFile("ok-product.json"))
        );

        mockRecommendationsApi.stubFor(
                get("/api/recommendations?productId=123")
                        .willReturn(ok()
                                .withHeader("Content-Type", "application/json")
                                .withBodyFile("ok-recommended.json")
                                .withFixedDelay(5000)) // Add a longer delay than the test timeout to the recommendations call. We expect it to fall back to a default.
        );

        given()
                .get("/shop/products/123")
                .then()
                .statusCode(200)
                .body("html.body.h1", is("Mac 'n Cheese Order Button"));
    }

}
