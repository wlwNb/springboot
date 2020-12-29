package wlw.zc.demo.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * redis锁工具类
 * Created by mawei on 2019/5/11 17:13
 */
@Slf4j
public class RedisUtil {

    private static final Long SUCCESS = 1L;


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
     * 获取分布式锁
     *
     * @param lockKey     锁
     * @param requestId   请求标识
     * @param expireTime  单位秒
     * @return 是否获取成功
     */
    public static Boolean tryGetDistributedLock(RedisTemplate redisTemplate, String lockKey, String requestId, int expireTime) {
        try {
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
