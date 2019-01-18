package resilience;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.restassured.RestAssured;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TestBase {

    protected HttpClient testClient;

    @Rule
    public WireMockRule mockProductApi = new WireMockRule(8100);

    @Rule
    public WireMockRule mockRecommendationsApi = new WireMockRule(8101);

    @Before
    public void init() {
        mockRecommendationsApi.stubFor(
            get(urlPathEqualTo("/recommendations"))
                .atPriority(10)
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("ok-recommended.json"))
        );

        RestAssured.baseURI = "http://localhost:9000";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        testClient = HttpClientBuilder.create()
                .disableAutomaticRetries()
                .disableRedirectHandling()
                .build();

    }

    protected HttpResponse executeGet(String relativeUrl) throws Exception {
        HttpUriRequest request = RequestBuilder.get(appUrl(relativeUrl)).build();
        return testClient.execute(request);
    }

    protected String appUrl(String path) {
        return "http://localhost:9000" + path;
    }
}
