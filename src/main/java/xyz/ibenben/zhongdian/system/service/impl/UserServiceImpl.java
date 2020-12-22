package xyz.ibenben.zhongdian.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xyz.ibenben.zhongdian.system.entity.User;
import xyz.ibenben.zhongdian.system.service.UserService;

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

