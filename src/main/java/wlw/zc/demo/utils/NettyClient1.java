package wlw.zc.demo.utils;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import wlw.zc.demo.netty.ClientHandler;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class NettyClient1 {
    private static final EventLoopGroup group = new NioEventLoopGroup();

    public static void main(String[] args) throws Exception {
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    // 自定义处理程序
                    socketChannel.pipeline().addLast(new FixedLengthFrameDecoder(2));
                    socketChannel.pipeline().addLast(new StringDecoder());
                    socketChannel.pipeline().addLast(new StringEncoder());
                    socketChannel.pipeline().addLast(new ClientHandler());
                }
            });
            // 绑定端口并同步等待
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1",9999)).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
