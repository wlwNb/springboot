package wlw.zc.demo.config.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
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
        consumer.setConsumeThreadMax(consumeThreadMax);
        consumer.registerMessageListener(msgListener);
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
