package wlw.zc.demo.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import server.OrderService;

@Service(version = "1.0.0")
public class OrderServiceImpl implements OrderService {
    @Override
    public String send() {
      return "调用成功";
    }
}
