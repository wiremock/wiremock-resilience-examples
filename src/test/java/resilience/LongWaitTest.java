package resilience;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LongWaitTest extends TestBase {

    @Test(timeout = 3000)
    public void returns_ok_when_product_response_is_delayed() {

    }

}
