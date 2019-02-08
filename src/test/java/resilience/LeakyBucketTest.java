package resilience;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LeakyBucketTest extends TestBase {

    @Test(timeout = 2000)
    public void returns_ok_with_fallback_data_when_multiple_proxy_errors_returned() {

    }

}
