package wlw.zc.demo.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.context.annotation.Import;
import server.OrderService;
import wlw.zc.demo.system.entity.Product;

@Service(version = "1.0.0")
public class OrderServiceImpl implements OrderService {
    @Override
    public String send() {
      return "调用成功";
    }
}
