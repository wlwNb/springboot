package wlw.zc.demo.thread;

import java.util.concurrent.*;

/**
 * 线程池的各种应用
 */
public class Executor {
    private final static ExecutorService fixedPool = Executors.newFixedThreadPool(10);

    private final static ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    /**
     * 至于这个返回的future，感觉唯一的作用就是就是用来判断任务是否已经执行完成
     * @param runnable
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    private static void submit(Runnable runnable) throws InterruptedException, ExecutionException, TimeoutException {
        Future<?> future = fixedPool.submit(runnable);
        future.get(10, TimeUnit.SECONDS);
    }

    /**
     * 至于这个返回的future，感觉更加鸡肋，还加了个泛型，这个根本都没有返回结果，不太清楚这个方法的设计目的
     * @param runnable
     * @param clazz
     * @param <T>
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    private static <T> void submit(Runnable runnable, T clazz) throws InterruptedException, ExecutionException, TimeoutException {
        Future<T> future = fixedPool.submit(runnable, clazz);
        future.get(10, TimeUnit.SECONDS);
    }

    private static <T> void submit(Callable<T> callable) throws InterruptedException, ExecutionException, TimeoutException {
        Future<T> future = fixedPool.submit(callable);
        System.out.println(future.get(10, TimeUnit.SECONDS));
    }

    private static void fifo(){

    }





    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        submit(() -> {
            return "success";
        });
    }
}
