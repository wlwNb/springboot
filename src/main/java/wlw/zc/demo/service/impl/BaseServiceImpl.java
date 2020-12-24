package wlw.zc.demo.service.impl;

import org.springframework.stereotype.Component;
import wlw.zc.demo.service.BaseService;

@Component
public class BaseServiceImpl implements BaseService {
    @Override
    public void send() {
        System.out.println("调用service服务,发送消息");
    }
}
