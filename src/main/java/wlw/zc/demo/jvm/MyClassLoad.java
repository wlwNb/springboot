package wlw.zc.demo.jvm;

import lombok.SneakyThrows;
import wlw.zc.demo.juc.aqs.Lock;

public class MyClassLoad extends ClassLoader {
    @SneakyThrows
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> aClass = loadClass("wlw.zc.demo.juc.aqs.Lock", false);
        Lock o = (Lock)aClass.newInstance();
        o.test5();
        return aClass;
    }

    public MyClassLoad() {
    }

    public MyClassLoad(ClassLoader parent) {
        super(parent);
    }
}
