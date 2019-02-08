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
public class DrippingTapTest extends TestBase {

    @Test(timeout = 3000)
    public void returns_ok_product_api_dribbles_response_over_5_seconds() {
        mockProductApi.stubFor(
                get(urlPathEqualTo("/api/products/123"))
                        .willReturn(ok()
                                .withHeader("Content-Type", "application/json")
                                .withBodyFile("ok-product.json")
                                .withChunkedDribbleDelay(5, 5000))
        );

        given()
                .get("/shop/products/123")
                .then()
                .assertThat()
                .statusCode(200)
                .body("html.body.h1", is("Mac 'n Cheese Order Button"));
    }

}
