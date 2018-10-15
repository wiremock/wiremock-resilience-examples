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
public class LeakyBucketTest extends TestBase {

    @Test(timeout = 2000)
    public void returns_ok_when_product_response_is_as_expected() throws Exception {
        mockProductApi.stubFor(
                get(urlPathEqualTo("/products/123"))
                    .willReturn(aResponse()
                            .withStatus(502))
        );

        List<Integer> statusCodes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            HttpResponse response = executeGet("/products/123");
            EntityUtils.consume(response.getEntity());
            statusCodes.add(response.getStatusLine().getStatusCode());
        }

        assertThat(statusCodes).allMatch(code -> code == 200);
    }

}
