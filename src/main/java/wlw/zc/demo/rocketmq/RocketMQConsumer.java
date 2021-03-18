package wlw.zc.demo.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class RocketMQConsumer {
    @Value("${rocketmq.name-server}")
    private String namesrvAddr;
    @Value("${rocketmq.consumer.wlw.group}")
    private String groupName;
    @Value("${rocketmq.consumer.wlw.consumeThreadMin}")
    private int consumeThreadMin;
    @Value("${rocketmq.consumer.wlw.consumeThreadMax}")
    private int consumeThreadMax;
    @Value("${rocketmq.consumer.wlw.topics}")
    private String topics;
    @Value("${rocketmq.consumer.wlw.consumeMessageBatchMaxSize}")
    private int consumeMessageBatchMaxSize;
    @Resource
    private RocketMsgListener msgListener;
    @Bean
    public DefaultMQPushConsumer getRocketMQConsumer(){
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumeThreadMin(consumeThreadMin);
        //consumer.registerMessageListener(msgListener);
        consumer.registerMessageListener((MessageListenerOrderly) (msgs, context) -> {
            //获取消息
            MessageExt msg = msgs.get(0);
            //消费者获取消息 这里只输出 不做后面逻辑处理
            log.info("Consumer-线程名称={},消息={}", Thread.currentThread().getName(), new String(msg.getBody()));
            return ConsumeOrderlyStatus.SUCCESS;
        });
        //consumer.registerMessageListener(msgListener);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
        try {
            String[] topicTagsArr = topics.split(";");
            for (String topicTags : topicTagsArr) {
                String[] topicTag = topicTags.split("~");
                consumer.subscribe(topicTag[0],"*");
            }
            consumer.start();
        }catch (MQClientException e){
            log.error("consumer exception==》",e);
        }
        return consumer;
    }
}
