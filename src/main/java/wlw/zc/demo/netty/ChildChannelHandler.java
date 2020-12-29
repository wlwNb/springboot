package wlw.zc.demo.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
    @Resource
    private NettyServerHandler nettyServerHandler;

    public void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new FixedLengthFrameDecoder(10));
        socketChannel.pipeline().addLast(new StringEncoder());
        socketChannel.pipeline().addLast(new StringDecoder());
        socketChannel.pipeline().addLast(new NettyServerHandler());
    }
}
