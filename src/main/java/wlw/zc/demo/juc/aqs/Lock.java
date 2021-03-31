package wlw.zc.demo.juc.aqs;

import lombok.SneakyThrows;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.LockSupport;

public class Lock {
    //资源状态标识
     private volatile int state;
     //阻塞队列放等待线程
     private LinkedBlockingQueue<Thread> queue = new LinkedBlockingQueue<>();
     //当前占有资源的线程
     private Thread thread;
     //内存操作工具
     //private static final Unsafe unsafe = Unsafe.getUnsafe();
     private static int i = 0;
     private static CountDownLatch countDownLatch = new CountDownLatch(50);
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


    public static void main(String[] args) throws InterruptedException {
        Lock lock = new Lock();
            for (int j = 0; j < 50; j++) {
                new Thread(new Runnable() {
                    @SneakyThrows
                    @Override
                    public void run() {
                        lock.lock();
                        for (int k = 0; k < 1000; k++) {
                            i++;
                        }
                        lock.unlock();
                        countDownLatch.countDown();
                    }
                }).start();
            }
            countDownLatch.await();
            System.out.println(i);
    }


}
