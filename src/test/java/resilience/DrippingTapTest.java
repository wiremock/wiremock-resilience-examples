package resilience;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DrippingTapTest extends TestBase {

    @Test(timeout = 6000)
    public void returns_ok_product_api_dribbles_response_over_5_seconds() {

    }

}
