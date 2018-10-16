package resilience;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LongWaitTest extends TestBase {


    @Test(timeout = 3000)
    public void returns_ok_when_product_response_is_as_expected() throws Exception {
        mockProductApi.stubFor(
                get(urlPathEqualTo("/products/123"))
                    .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("ok-product.json"))
        );

        mockRecommendationsApi.stubFor(
                get("/recommendations?productId=123")
                        .willReturn(ok()
                                .withHeader("Content-Type", "application/json")
                                .withBodyFile("ok-recommended.json")
                                .withFixedDelay(5000))
        );

        HttpResponse response = executeGet("/products/123");

        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(500);
    }

}
