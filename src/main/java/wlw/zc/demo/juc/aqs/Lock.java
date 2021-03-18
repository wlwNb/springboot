package wlw.zc.demo.juc.aqs;

import com.google.common.collect.HashBasedTable;
import lombok.SneakyThrows;
import sun.misc.Unsafe;
import wlw.zc.demo.system.entity.Task;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class Lock extends Task {
    //资源状态标识
     private volatile int state;
     //阻塞队列放等待线程
     private LinkedBlockingQueue<Thread> queue = new LinkedBlockingQueue<>();
     //当前占有资源的线程
     private Thread thread;
     //内存操作工具
     //private static final Unsafe unsafe = Unsafe.getUnsafe();
     private static int i = 0;
     private static CountDownLatch countDownLatch = new CountDownLatch(1000);
     private void lock() throws InterruptedException {
          if(tryLock()){
              return;
         }
         if(!queue.contains(Thread.currentThread())) {
             queue.put(Thread.currentThread());
         }
         LockSupport.park();
         lock();
     }

     private boolean tryLock() {
         if(state == 0){
           if(compareAndSwapState(0,1)){
               thread = Thread.currentThread();
               queue.remove(Thread.currentThread());
               return  true;
           }
         }
         return false;
     }

     private void unlock(){
         if(state == 0 || thread != Thread.currentThread()){
             return;
         }
         if(compareAndSwapState(state,0)){
             thread = null;
         }
         Iterator<Thread> iterator = queue.iterator();
         while (iterator.hasNext()){
             Thread thread = iterator.next();
             queue.remove(thread);
             LockSupport.unpark(thread);
         }
     }

     private void addMap(Map map){
         map.get();
     }

    /**
     * 原子修改状态,unsafe类只能通过反射来进行操作
     */
    public final boolean compareAndSwapState(int except, int update) {
        return unsafe.compareAndSwapInt(this, stateOffset, except, update);
    }


    private static  Unsafe unsafe ;

    private static long stateOffset = 0;

    static {
        try {
            Field theUnsafeInstance = null;
            try {
                theUnsafeInstance = Unsafe.class.getDeclaredField("theUnsafe");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            theUnsafeInstance.setAccessible(true);
            try {
                unsafe = (Unsafe) theUnsafeInstance.get(Unsafe.class);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            stateOffset = unsafe.objectFieldOffset(Lock.class.getDeclaredField("state"));
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    static class MyThreadTest extends Thread{
        private CountDownLatch countDownLatchBefore;
        private CountDownLatch countDownLatchAfter;

        @SneakyThrows
        @Override
        public void run() {
            CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
            countDownLatchBefore.await();
            countDownLatchAfter.countDown();
            System.out.println(Thread.currentThread().getName());
        }

        public MyThreadTest(String name,CountDownLatch countDownLatch1,CountDownLatch countDownLatch2) {
            this.countDownLatchBefore = countDownLatch1;
            this.countDownLatchAfter = countDownLatch2;
        }
    }

    static class MyThreadTest2 extends Thread{
        private Thread t;
        @SneakyThrows
        @Override
        public void run() {
            if(Objects.nonNull(t))
            t.join();
            System.out.println(Thread.currentThread().getName());
        }
        private void test(){
            System.out.println("======test=====");
        }

        public MyThreadTest2(String name,Thread t) {
            this.t = t;
        }

        public MyThreadTest2( ) {
        }
    }

    private void test(){
          //控制线程顺序执行 CountDownLatch
        CountDownLatch first = new CountDownLatch(0);
        for (int j = 0; j <5 ; j++) {
            CountDownLatch next =  new CountDownLatch(1);
            new MyThreadTest("Thread-"+j,first,next).start();
            first = next;
        }
        //控制线程顺序执行 Join
        MyThreadTest2 firstThread = null;
        for (int j = 5; j > 0 ; j--) {
            MyThreadTest2 myThreadTest2 = new MyThreadTest2("Thread-" + j, firstThread);
            myThreadTest2.start();
            firstThread = myThreadTest2;
        }
    }

    private void test2() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
          Class myThreadTest2Class = Class.forName("wlw.zc.demo.juc.aqs.Lock$MyThreadTest2");
        Method run = myThreadTest2Class.getDeclaredMethod("test", null);
        Object o = myThreadTest2Class.newInstance();
        synchronized (o){
            o.notify();
        }
        run.setAccessible(true);
        run.invoke(o);

        /*  Class myThreadTest2Class1 = Class.forName("wlw.zc.demo.juc.aqs.Lock$MyThreadTest2");
        Method run = myThreadTest2Class1.getDeclaredMethod("test", null);
        Object o = myThreadTest2Class.newInstance();
        synchronized (o){
            o.notify();
        }
        run.setAccessible(true);
        run.invoke(o);*/
    }

    private void test3(){
           Object synObj = new Object();
        Object synObj2 = new Object();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized(synObj) {
                    System.out.println("1.T1获取synObj的对象监视器，开始执行同步块");
                    try {
                        TimeUnit.SECONDS.sleep(2);//休息一分钟,不放弃锁
                        System.out.println("T1在 wait()时挂起了");
                        synObj.wait();
                        System.out.println("T1被其他线程唤醒后并重新获得synObj的对象监视器，继续执行");
                    }catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("T1获取synObj的对象监视器，结束同步块");
                }
            };
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("T2启动，但是因为有别的线程占用了synObj的对象监视器，则等待别的线程执行synObj.wait来释放它");
                synchronized(synObj) {
                    try {
                        System.out.println("T2获取synObj的对象监视器，进入同步块");
                        synObj.notify();
                        System.out.println("T2执行synObj.notify()");
                        TimeUnit.SECONDS.sleep(2);
                        System.out.println("T2结束同步块，释放synObj的对象监视器");
                    }catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
        });
        t2.start();
    }

    private static final ReentrantLock reentrantLock = new ReentrantLock();
    private static final Condition condition = reentrantLock.newCondition();
    private static Queue<String> stringQueue = new LinkedList<>();

    public static void addTask(int i) {
        reentrantLock.lock();
        try {
            i++;
            System.out.println(i);
            condition.signalAll();
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
        }
    }

    public static String getTask(int i) throws InterruptedException {
        reentrantLock.lock();
        try {
             i++;
            System.out.println(i);
            condition.await();
            condition.signalAll();
            return "stringQueue.remove()";
        } finally {
            reentrantLock.unlock();
        }
    }

     static volatile int j = 0;

    private static void test4(){
      new Thread(()->{
          try {
              getTask(j);
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
      }).start();
        new Thread(()->{
            addTask(j);
        }).start();

    }

    public static void main(String[] args) throws InterruptedException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        test4();
        /*Lock lock = new Lock();
        for (int j = 0; j <1000 ; j++) {
            new Thread(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    lock.lock();
                    for (int k = 0; k <1000 ; k++) {
                        i++;
                    }
                    lock.unlock();
                    countDownLatch.countDown();
                }
            }).start();
        }
        countDownLatch.await();
        System.out.println(i);
    }*/
    }

}
