package resilience;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class FlakeTest extends TestBase {


    @Test
    public void returns_ok_when_product_response_is_as_expected() throws Exception {
        mockProductApi.stubFor(
                get(urlPathEqualTo("/products/123"))
                    .inScenario("the-flake")
                    .willSetStateTo("DOWN")
                    .whenScenarioStateIs(STARTED)
                    .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("ok-product.json"))
        );

        mockProductApi.stubFor(
                get(urlPathEqualTo("/products/123"))
                        .inScenario("the-flake")
                        .willSetStateTo(STARTED)
                        .whenScenarioStateIs("DOWN")
                        .willReturn(serviceUnavailable()
                                .withHeader("Content-Type", "text/plain")
                                .withBody("bleargh!"))
        );


        List<Integer> statusCodes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            HttpResponse response = executeGet("/products/123");
            EntityUtils.consume(response.getEntity());
            statusCodes.add(response.getStatusLine().getStatusCode());
        }

        assertThat(statusCodes).allMatch(code -> code == 200);
    }

}
