package wlw.zc.demo.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLog {
    //配置菜单级别，多级菜单可以用：分割
    String menu();
    //配置具体的操作行为
    String operation();
}
