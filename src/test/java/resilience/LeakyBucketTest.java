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
public class LeakyBucketTest extends TestBase {

    @Test(timeout = 2000)
    public void returns_ok_with_fallback_data_when_multiple_proxy_errors_returned() {
        mockProductApi.stubFor(
                get(urlPathEqualTo("/api/products/123"))
                        .willReturn(aResponse()
                                .withStatus(502))
        );

        for (int i = 1; i <= 10; i++) {
            System.out.println("Fetch count: " + i);
            given().get("/shop/products/123");
        }

        given()
                .get("/shop/products/123")
                .then()
                .assertThat()
                .statusCode(200)
                .body("html.body.h1", is("Mac 'n Cheese Order Button"));
    }

}
