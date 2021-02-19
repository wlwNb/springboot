package wlw.zc.demo.juc.queue;

import java.util.concurrent.LinkedBlockingQueue;

public class MyLinkedBlockingQueue extends LinkedBlockingQueue<String> {

    public MyLinkedBlockingQueue(int capacity) {
        super(capacity);
    }

    public static void main(String[] args) throws InterruptedException {
        MyLinkedBlockingQueue m = new MyLinkedBlockingQueue(5);
        m.offer("111");
        m.offer("222");
        m.element();
        m.size();
    }
}
