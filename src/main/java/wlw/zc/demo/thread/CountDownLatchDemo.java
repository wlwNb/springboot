package wlw.zc.demo.thread;

import org.apache.shiro.authc.SimpleAuthenticationInfo;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * CountDownLatch 也可以控制线程顺序执行
 */
public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch readyLatch = new CountDownLatch(5);
        CountDownLatch runningLatchWait = new CountDownLatch(1);
        CountDownLatch completeLatch = new CountDownLatch(5);


        List<Thread> workers = Stream.generate(() -> new Thread(new CountDownDemo2(readyLatch, runningLatchWait, completeLatch))).limit(5).collect(Collectors.toList());
        workers.forEach(Thread::start);
        readyLatch.await();//等待发令
        runningLatchWait.countDown();//发令
        completeLatch.await();//等所有子线程执行完

        System.out.println("主线程执行完毕");

    }

    static class CountDownDemo2 implements Runnable {
        private CountDownLatch readyLatch;
        private CountDownLatch runningLatchWait;
        private CountDownLatch completeLatch;

        public CountDownDemo2(CountDownLatch readyLatch, CountDownLatch runningLatchWait, CountDownLatch completeLatch) {
            this.readyLatch = readyLatch;
            this.runningLatchWait = runningLatchWait;
            this.completeLatch = completeLatch;
        }

        @Override
        public void run() {
            System.out.println("线程" + Thread.currentThread().getName() + "准备入场");
            readyLatch.countDown();
            try {
                System.out.println("线程" + Thread.currentThread().getName() + "在预备");
                runningLatchWait.await();
                System.out.println("线程" + Thread.currentThread().getName() + "开始执行");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                completeLatch.countDown();
            }
        }
    }
}
