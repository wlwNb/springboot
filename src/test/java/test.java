import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import wlw.zc.demo.CustomerApplication;
import wlw.zc.demo.system.service.LoginService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CustomerApplication.class})
public class test {
    @Autowired
    private LoginService loginService;

    @Test
    public void test(){
        loginService.getUserByName("111");
    }
}
