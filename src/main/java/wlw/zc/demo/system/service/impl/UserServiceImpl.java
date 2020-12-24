package wlw.zc.demo.system.service.impl;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import wlw.zc.demo.system.entity.User;
import wlw.zc.demo.system.service.UserService;

import javax.annotation.Resource;


@Service
public class UserServiceImpl implements UserService{
	@Resource
	private RedisTemplate redisTemplate;

	public void saveUser(User user){
		redisTemplate.opsForValue().set("name","wangwu");
		return;
	}
}

