package wlw.zc.demo.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;

@Slf4j
@Component
public class NettyServer {
    @Resource
    private ChildChannelHandler childChannelHandler;
    @Resource
    private ExecutorService executor;
    @Resource
    private HeartbeatInitializer heartbeatInitializer;
    //启动Netty Server
    @PostConstruct
    public void run() throws Exception {
        /**
         * NioEventLoop并不是一个纯粹的I/O线程，它除了负责I/O的读写之外
         * 创建了两个NioEventLoopGroup，
         * 它们实际是两个独立的Reactor线程池。
         * 一个用于接收客户端的TCP连接，
         * 另一个用于处理I/O相关的读写操作，或者执行系统Task、定时任务Task等。
         */
        //boss 线程组用于处理连接工作
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //work 线程组用于数据处理
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        log.info("netty 准备运行端口：" + 9990);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerBootstrap bootstrap = new ServerBootstrap();
                    bootstrap.group(bossGroup, workerGroup)
                            //指定Channel
                            .channel(NioServerSocketChannel.class)
                            /*ChannelOption.SO_BACKLOG对应的是tcp/ip协议listen函数中的backlog参数。函数listen(int socketfd, int backlog)用来初始化服务端可连接队列。
                            服务端处理客户端连接请求是顺序处理的，所以同一时间只能处理一个客户端连接，
                            多个客户端来的时候，服务端将不能处理的客户端连接请求放在队列中等待处理，backlog参数指定了队列的大小。*/
                            .childOption(ChannelOption.SO_BACKLOG,128)
                            /*ChannelOption.SO_BACKLOG对应的是tcp/ip协议listen函数中的backlog参数。函数listen(int socketfd, int backlog)用来初始化服务端可连接队列。
                             服务端处理客户端连接请求是顺序处理的，所以同一时间只能处理一个客户端连接，多个客户端来的时候，服务端将不能处理的客户端连接请求放在队列中等待处理，backlog参数指定了队列的大小。*/
                            .childOption(ChannelOption.SO_KEEPALIVE,true)
                            .childHandler(new HeartbeatInitializer());
                    //绑定端口，同步等待成功
                    ChannelFuture f = bootstrap.bind("127.0.0.1",9990).sync();
                    //等待服务监听端口关闭
                    f.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    //退出，释放线程资源
                    workerGroup.shutdownGracefully();
                    bossGroup.shutdownGracefully();
                }
            }
        });
    }

    public static void main(String[] args) throws Exception {
        //run();
    }
}