package wlw.zc.demo.system.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import server.OrderService;
import wlw.zc.demo.config.UserRealm;
import wlw.zc.demo.netty.NettyServer;
import wlw.zc.demo.socket.NIOLowServer;
import wlw.zc.demo.spring.SpringApplicationContext;
import wlw.zc.demo.system.entity.UploadFile;
import wlw.zc.demo.system.entity.User;
import wlw.zc.demo.system.service.UserService;
import wlw.zc.demo.utils.RedisUtil;

import javax.annotation.Resource;

@RestController
@Slf4j
public class UserController {
	@Autowired
	private UserRealm userRealm;
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	@Qualifier("mqProducer")
	private DefaultMQProducer producer;
	@Resource
	private NettyServer discardServer;
	@Resource
	private NIOLowServer nioLowServer;
	@Autowired
	private RedissonClient redissonClient;
	@Reference(version = "1.0.0")
	private OrderService orderService;
	@Resource
	private UserService userService;
	@Resource
	private TransactionMQProducer transactionMQProducer;
	@RequestMapping("/regiester")
	public String regiester(Map<String, Object> model){
		redisTemplate.opsForValue().set("id","1111111111");
		redisTemplate.opsForValue().get("id");
		return "success";
	}
	@RequestMapping("/send")
	public void send(Map<String, Object> model) throws UnsupportedEncodingException, InterruptedException, RemotingException, MQClientException, MQBrokerException {
		System.out.println(orderService.send());
		//SpringApplicationContext.getBean(Product.class);
		List<ProductOrder> orderList = new ArrayList<>();
		orderList.add(new ProductOrder("XXX001", "订单创建"));
		orderList.add(new ProductOrder("XXX001", "订单付款"));
		orderList.add(new ProductOrder("XXX001", "订单完成"));
		orderList.add(new ProductOrder("XXX002", "订单创建"));
		orderList.add(new ProductOrder("XXX002", "订单付款"));
		orderList.add(new ProductOrder("XXX002", "订单完成"));
		orderList.add(new ProductOrder("XXX003", "订单创建"));
		orderList.add(new ProductOrder("XXX003", "订单付款"));
		orderList.add(new ProductOrder("XXX003", "订单完成"));
		for (ProductOrder v:orderList) {
			Message msg = new Message("topic_test",
					"",
					"",
					JSON.toJSONString(v).getBytes(RemotingHelper.DEFAULT_CHARSET));
					transactionMQProducer.send(msg, new MessageQueueSelector() {
						@Override
						public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
							//3、arg的值其实就是下面传入 orderId
							String orderid = (String) o;
							//4、因为订单是String类型，所以通过hashCode转成int类型
							int hashCode = orderid.hashCode();
							//5、因为hashCode可能为负数 所以取绝对值
							hashCode = Math.abs(hashCode);
							//6、保证同一个订单号 一定分配在同一个queue上
							long index = hashCode % list.size();
							return list.get((int) index);
						}
					},v.getOrderId(),5000);
		}

	}

	class ProductOrder{
		private String orderId;
		private String remark;

		public ProductOrder(String orderId, String remark) {
			this.orderId = orderId;
			this.remark = remark;
		}

		public String getOrderId() {
			return orderId;
		}

		public String getRemark() {
			return remark;
		}
	}

	@GetMapping("/select")
	@RequiresPermissions("query")
	public String select(){
		RLock lock = redissonClient.getLock("1");
		lock.lock();
		//List<SaasMedicalProduct> saasMedicalProducts = mapper.listDataByIds(Arrays.asList(111732909L, 111732937L));
		//return JSON.toJSONString(saasMedicalProducts);
		return null;
	}

	@PostMapping("/importFile")
	public String importFile(UploadFile uploadFile){
		RLock lock = redissonClient.getLock("1");
		lock.lock();
		//List<SaasMedicalProduct> saasMedicalProducts = mapper.listDataByIds(Arrays.asList(111732909L, 111732937L));
		return null;
		//return JSON.toJSONString(saasMedicalProducts);
	}

	@PostMapping("/jedis")
	public void jedis(){
		userService.saveUser(null);
		String s = UUID.randomUUID().toString();
		RedisUtil.tryGetDistributedLock(redisTemplate,"ttt",s,100);
		Jedis jedis = SpringApplicationContext.getBean(JedisPool.class).getResource();
		RedisUtil.tryLock(jedis,"test", s);
		RedisUtil.releaseDistributedLock(jedis,"test",s);
	}

	public static void main(String[] args) {

	}


	@PostMapping("/login")
	public String login(User user){
		if (StringUtils.isEmpty(user.getUserName()) || StringUtils.isEmpty(user.getPassword())) {
			return "请输入用户名和密码！";
		}
		//用户认证信息
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(
				user.getUserName(),
				user.getPassword()
		);
		try {
			//进行验证，这里可以捕获异常，然后返回对应信息
			subject.login(usernamePasswordToken);
//            subject.checkRole("admin");
//            subject.checkPermissions("query", "add");
		} catch (UnknownAccountException e) {
			log.error("用户名不存在！", e);
			return "用户名不存在！";
		} catch (AuthenticationException e) {
			log.error("账号或密码错误！", e);
			return "账号或密码错误！";
		} catch (AuthorizationException e) {
			log.error("没有权限！", e);
			return "没有权限";
		}
		return "login success";
	}


	@GetMapping("error")
	public String error(){
		return "登陆失败";
	}
}
