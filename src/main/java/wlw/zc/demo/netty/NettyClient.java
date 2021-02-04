package wlw.zc.demo.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class NettyClient {
    @Resource
    private ExecutorService executor;
    private NioEventLoopGroup workGroup = new NioEventLoopGroup(4);
    private Channel channel;
    private Bootstrap bootstrap;

    @PostConstruct
    public void init() throws Exception {
        start();
        sendData();
    }
    public void sendData() throws Exception {
        executor.execute(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                Random random = new Random(System.currentTimeMillis());
                for (int i = 0; i < 10000; i++) {
                    if (channel != null && channel.isActive()) {
                        String content = "client msg " + i;
                        ByteBuf buf = channel.alloc().buffer(5 + content.getBytes().length);
                        buf.writeInt(5 + content.getBytes().length);
                        buf.writeByte(CustomHeartbeatHandler.CUSTOM_MSG);
                        buf.writeBytes(content.getBytes());
                        channel.writeAndFlush(content);
                    }
                    Thread.sleep(random.nextInt(20000));
                }
            }
        });
    }

    public void start() {
        try {
            bootstrap = new Bootstrap();
            bootstrap
                    .group(workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            p.addLast(new IdleStateHandler(0, 0, 3));
                            p.addLast(new StringEncoder(StandardCharsets.UTF_8));
                            p.addLast(new StringDecoder(StandardCharsets.UTF_8));
                            p.addLast(new ClientHandler());
                        }
                    });
            doConnect();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void doConnect() {
        if (channel != null && channel.isActive()) {
            return;
        }

        ChannelFuture future = bootstrap.connect("127.0.0.1", 9999);

        future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture futureListener) throws Exception {
                if (futureListener.isSuccess()) {
                    channel = futureListener.channel();
                    System.out.println("链接成功。。。");
                } else {
                    System.out.println("尝试重连中。。。");

                    futureListener.channel().eventLoop().schedule(new Runnable() {
                        @Override
                        public void run() {
                            doConnect();
                        }
                    }, 1, TimeUnit.SECONDS);
                }
            }
        });
    }
}
