package wlw.zc.demo.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MyJdkProxy implements InvocationHandler {
    private Object targetObject;
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("JDK动态代理，监听开始！");
        Object result = method.invoke(targetObject, args);
        System.out.println("JDK动态代理，监听结束！");
        return result;
    }
    public Object  getMyProxy(Object target){
        this.targetObject = target;
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),target.getClass().getInterfaces(),this);
    }


    public static void main(String[] args) {
        MyJdkProxy myJdkProxy = new MyJdkProxy();
        Target proxy = (Target) myJdkProxy.getMyProxy(new TargetImpl());
        proxy.get();

    }
}
