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
public class ExampleTest extends TestBase {

    @Test
    public void returns_ok_when_product_response_is_as_expected() {
        mockRecommendationsApi.stubFor(
                get(urlPathEqualTo("/api/recommendations"))
                        .willReturn(ok()
                                .withHeader("Content-Type", "application/json")
                                .withBodyFile("ok-recommended.json"))
        );

        mockProductApi.stubFor(
                get(urlPathEqualTo("/api/products/123"))
                        .willReturn(ok()
                                .withHeader("Content-Type", "application/json")
                                .withBodyFile("ok-product.json"))
        );

        given()
                .get("/shop/products/123")
                .then()
                .assertThat()
                .statusCode(200)
                .body("html.body.h1", is("Mac 'n Cheese Order Button"))
                .body("html.body.div.find { it.@id == 'recommendations' }.strong", is("Waffle Iron"));
    }
}
