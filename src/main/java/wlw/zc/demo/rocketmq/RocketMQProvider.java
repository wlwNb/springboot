package wlw.zc.demo.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;


@Component
@Slf4j
public class RocketMQProvider {

    /**
     * 生产者的组名
     */
    @Value("${rocketmq.producer.wlw.group}")
    private String producerGroup;

    /**
     * NameServer 地址
     */
    @Value("${rocketmq.name-server}")
    private String namesrvAddr;
    // @PostConstruct //@PostContruct是spring框架的注解，在方法上加该注解会在项目启动的时候执行该方法，也可以理解为在spring容器初始化的时候执行该方法。

    @Autowired
    private TransactionListenerImpl transactionListener;
    @Bean
    public DefaultMQProducer mqProducer() {
        //生产者的组名
        DefaultMQProducer producer = new DefaultMQProducer(producerGroup);

        //指定NameServer地址，多个地址以 ; 隔开
        producer.setNamesrvAddr(namesrvAddr);
        try {
            producer.start();
            /**
             * Producer对象在使用之前必须要调用start初始化，初始化一次即可
             * 注意：切记不可以在每次发送消息时，都调用start方法
             */
            //创建一个消息实例，包含 topic、tag 和 消息体
            //如下：topic 为 "TopicTest"，tag 为 "push"
        } catch (Exception e) {
           log.info("rocketmq Provider exception==>",e);
        }
        return producer;
    }

    @Bean
    public TransactionMQProducer transactionProducer () {
        //生产者的组名
        TransactionMQProducer producer = new TransactionMQProducer(producerGroup);
        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("client-transaction-msg-check-thread");
                return thread;
            }
        });
        producer.setNamesrvAddr(namesrvAddr);
        producer.setExecutorService(executorService);
        producer.setTransactionListener(transactionListener);
        //指定NameServer地址，多个地址以 ; 隔开
        producer.setNamesrvAddr(namesrvAddr);
        try {
            producer.start();
            /**
             * Producer对象在使用之前必须要调用start初始化，初始化一次即可
             * 注意：切记不可以在每次发送消息时，都调用start方法
             */
            //创建一个消息实例，包含 topic、tag 和 消息体
            //如下：topic 为 "TopicTest"，tag 为 "push"
        } catch (Exception e) {
            log.info("rocketmq Provider exception==>",e);
        }
        return producer;
    }

}
