package wlw.zc.demo.config;

import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(value = AuthorizationException.class)
    @ResponseBody
    public String exceptionHandler(Exception e){
        System.out.println("未知异常！原因是:"+e);
        return "无权限！";
    }
}
