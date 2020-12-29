package wlw.zc.demo.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class HeartbeatInitializer extends ChannelInitializer<Channel> {
    @Resource
    private NettyServerHandler nettyServerHandler;
    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline()
                //五秒没有收到消息 将IdleStateHandler 添加到 ChannelPipeline 中
                .addLast(new IdleStateHandler(5, 0, 0))//(8)
                .addLast(new StringEncoder())//(9)
                .addLast(new StringDecoder())//(9)
                .addLast(new NettyServerHandler());//(10)
    }
    }
