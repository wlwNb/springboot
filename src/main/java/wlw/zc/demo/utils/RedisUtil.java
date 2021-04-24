package wlw.zc.demo.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;
import wlw.zc.demo.spring.SpringApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * redis锁工具类
 * Created by mawei on 2019/5/11 17:13
 */
@Slf4j
public class RedisUtil {

    private static final Long SUCCESS = 1L;
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final String LOCK_PREFIX = "wlw_";
    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
    private static volatile Integer maxId = 0;
    private static volatile AtomicInteger minId = new AtomicInteger(0);
    private static final int add = 1000;
    private static ReentrantLock lock = new ReentrantLock();

    /**
     * 获取分布式id
     * @return
     * @throws InterruptedException
     */
    public static Integer getId() throws InterruptedException {
        if(minId.get()>maxId){
            try {
                if(lock.tryLock()) {
                    addId();
                }else {
                    Thread.sleep(50);
                    getId();
                }
            } finally {
                lock.unlock();
            }
        }
        return minId.getAndIncrement();
    }

    private static void addId(){
         /*
             Integer id = redis.increby(1000);
            */
        Integer id =0;
        maxId = id;
        minId.set(id-add);
    }


    /**
     * 加锁
     * @param key 被秒杀商品的id
     * @param value 当前线程操作时的 System.currentTimeMillis() + 2000，2000是超时时间，这个地方不需要去设置redis的expire，
     *              也不需要超时后手动去删除该key，因为会存在并发的线程都会去删除，造成上一个锁失效，结果都获得锁去执行，并发操作失败了就。
     * @return
     */
    public boolean lock(StringRedisTemplate redisTemplate, String key, String value) {
        //如果key值不存在，则返回 true，且设置 value
        if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
            return true;
        }
        //TODO 有问题，value是System.currentTimeMillis() + 2000，在集群情况下，可能出现主机时间不一致的情况，getAndSet会覆盖原值。
        //获取key的值，判断是是否超时
        String curVal = redisTemplate.opsForValue().get(key);
        if (StringUtils.isNotEmpty(curVal) && Long.parseLong(curVal) < System.currentTimeMillis()) {
            //获得之前的key值，同时设置当前的传入的value。这个地方可能几个线程同时过来，但是redis本身天然是单线程的，所以getAndSet方法还是会安全执行，
            //首先执行的线程，此时curVal当然和oldVal值相等，因为就是同一个值，之后该线程set了自己的value，后面的线程就取不到锁了
            String oldVal = redisTemplate.opsForValue().getAndSet(key, value);
            if(StringUtils.isNotEmpty(oldVal) && oldVal.equals(curVal)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param redisTemplate
     * @param key
     * @param expireTime
     * @return
     * 1.原子性
     * 2.误删
     * 3.可重入
     * 4.超时
     */
    public static boolean lock(RedisTemplate redisTemplate, String key , int expireTime) {
        if(threadLocal.get() == null){
            String s = UUID.randomUUID().toString();
            Boolean f = redisTemplate.opsForValue().setIfAbsent(key,s,expireTime,TimeUnit.SECONDS);
            if(f){
                threadLocal.set(1);
                Thread demo = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            Boolean expire = redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
                            //有可能已经主动删除key,不需要在续命
                            if(!expire){
                                return;
                            }
                            System.out.printf("续命成功");
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                demo.setDaemon(true);
                demo.start();

            }
            return f;
        }else{
            threadLocal.set(threadLocal.get()+1);
            return true;
        }
    }

    /**
     * 释放锁
     *
     * @param lockKey   锁
     * @return 是否释放成功
     * 支持可重入
     */
    public static boolean releaseLock(RedisTemplate redisTemplate, String lockKey) {

        try {
            if(threadLocal.get() != null){
                if(threadLocal.get()-1 == 0){
                    return  redisTemplate.opsForValue().getOperations().delete(lockKey);
                }else {
                    threadLocal.set(threadLocal.get()-1);
                    return true;
                }
            }
        } catch (Throwable e) {
            log.error("[redis分布式锁] 解锁异常, {}", e.getMessage(), e);
        }
        return  false;
    }

    public static  boolean tryLock(Jedis jedis,String key, String request) {
        Transaction transaction = jedis.multi();
        Response<String> response = transaction.set(LOCK_PREFIX + key, request, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, 10000);
        transaction.get(LOCK_PREFIX + key);
        transaction.exec();
        response.get();
        if ("OK".equals("result")){
            return true ;
        }else {
            return false ;
        }
    }

    /**
     * 释放分布式锁
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public static boolean releaseDistributedLock(Jedis jedis,String lockKey, String requestId) {
        Transaction transaction = jedis.multi();
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        //Object result = jedis.eval(script, Collections.singletonList(LOCK_PREFIX+lockKey), Collections.singletonList(requestId));
        transaction.setnx("111","2222");
        //transaction.eval(script, Collections.singletonList(LOCK_PREFIX+lockKey), Collections.singletonList(requestId));
        transaction.exec();
        jedis.get(LOCK_PREFIX+lockKey);
        if (SUCCESS==1) {
            return true;
        }
        return false;

    }



    /**
     * 获取分布式锁
     *
     * @param lockKey     锁
     * @param requestId   请求标识
     * @param expireTime  单位秒
     * @return 是否获取成功
     */
    public static Boolean tryGetDistributedLock(RedisTemplate redisTemplate, String lockKey, String requestId, int expireTime) {
        try {
            Object execute = redisTemplate.execute(new SessionCallback<Object>() {
                @Override
                public Object execute(RedisOperations redisOperations) throws DataAccessException {
                    redisOperations.multi();
                    //1、设置key
                    redisOperations.opsForValue().set("vKey", "hi redis.");
                    //2、获取key
                    redisOperations.opsForValue().get("vKey");
                    //3、删除key
                    redisOperations.delete("vKey");
                    final List<Object> result = redisOperations.exec();
                    //返回vKey的值
                    return result.get(1).toString();
                }
            });
            return redisTemplate.opsForValue().setIfAbsent(lockKey, requestId);
        } catch (Exception e) {
            log.error("尝试获取分布式锁-key[{}],异常===>{}", lockKey,e);
        }

        return false;
    }


    /**
     * 释放锁
     *
     * @param lockKey   锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public static boolean releaseLock(RedisTemplate redisTemplate, String lockKey, String requestId) {

        try {
            String currentValue = (String) redisTemplate.opsForValue().get(lockKey);
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(requestId)) {
                redisTemplate.opsForValue().getOperations().delete(lockKey);
                return true;
            }
        } catch (Throwable e) {
            log.error("[redis分布式锁] 解锁异常, {}", e.getMessage(), e);
        }
        return  false;
}
}
