package wlw.zc.demo.system.service.impl;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import wlw.zc.demo.spring.SpringApplicationContext;
import wlw.zc.demo.system.entity.User;
import wlw.zc.demo.system.service.UserService;
import wlw.zc.demo.utils.RedisUtil;

import javax.annotation.Resource;
import java.util.UUID;


@Service
public class UserServiceImpl implements UserService{
	@Resource
	private RedisTemplate redisTemplate;

	public void saveUser(User user){
		String s = UUID.randomUUID().toString();
		//RedisUtil.tryGetDistributedLock(redisTemplate,"ttt",s,100);
		redisTemplate.opsForValue().set("name","wangwu");
		redisTemplate.opsForSet();
		return;
	}
}

