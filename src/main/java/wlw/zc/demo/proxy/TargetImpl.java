package wlw.zc.demo.proxy;

public class TargetImpl implements Target {
    @Override
    public void get() {
        System.out.println("==========get==============");
    }

    public void set(){
        System.out.println("==========set==============");
    }
}
