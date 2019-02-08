package resilience;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class FlakeTest extends TestBase {

    @Test
    public void returns_ok_for_multiple_fetches_when_product_api_is_flakey() {
        mockProductApi.stubFor(
                get(urlPathEqualTo("/api/products/123"))
                        .inScenario("the-flake")
                        .willSetStateTo("DOWN")
                        .whenScenarioStateIs(STARTED)
                        .willReturn(ok()
                                .withHeader("Content-Type", "application/json")
                                .withBodyFile("ok-product.json"))
        );

        mockProductApi.stubFor(
                get(urlPathEqualTo("/api/products/123"))
                        .inScenario("the-flake")
                        .willSetStateTo(STARTED)
                        .whenScenarioStateIs("DOWN")
                        .willReturn(serviceUnavailable()
                                .withHeader("Content-Type", "text/plain")
                                .withBody("bleargh!"))
        );

        for (int i = 0; i < 5; i++) {
            given()
                    .get("/shop/products/123")
                    .then()
                    .assertThat()
                    .statusCode(200)
                    .body("html.body.h1", is("Mac 'n Cheese Order Button"));
        }

    }

}
