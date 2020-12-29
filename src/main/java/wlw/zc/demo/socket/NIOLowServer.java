package wlw.zc.demo.socket;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * 该服务启动，将不使用select选择器
 */
@Slf4j
@Component
public class NIOLowServer {
    @Resource
    private Executor executor;
    private Selector selector;
    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);//调整缓存的大小可以看到打印输出的变化
    private ByteBuffer sendBuffer = ByteBuffer.allocate(1024);//调整缓存的大小可以看到打印输出的变化
    private List<SocketChannel> clients = new LinkedList<>();
    @PostConstruct
    public void start() throws IOException, InterruptedException {
        executor.execute(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                // 打开服务器套接字通道
                ServerSocketChannel ssc = ServerSocketChannel.open();
                // 服务器配置为非阻塞
                ssc.configureBlocking(false);
                // 进行服务的绑定
                ssc.bind(new InetSocketAddress("localhost", 8001));
                log.info("nio low 准备运行端口 8001");
                // 通过open()方法找到Selector
                //selector = Selector.open();
                // 注册到selector，等待连接
                //ssc.register(selector, SelectionKey.OP_ACCEPT);
                 //TODO 不使用select选择器，导致大多数时候空轮训，效率低下
                while(true){
                    SocketChannel socketChannel = ssc.accept();//不会阻塞
                    if(socketChannel == null){
                        Thread.sleep(1000);
                        //log.info("无连接");
                    }else{
                        log.info("新连接来啦 ip:{},port:{}",socketChannel.socket().getInetAddress().getHostAddress(),socketChannel.socket().getPort());
                        //通道设置为非阻塞
                        socketChannel.configureBlocking(false);
                        clients.add(socketChannel);
                    }
                    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);//申请内存，可以是堆内，也可以是堆外
                    clients.forEach(channel->{
                        try {
                            int num = channel.read(byteBuffer);
                            if(num>0){
                                byteBuffer.flip();
                                byte[] bytes = new byte[byteBuffer.limit()];
                                byteBuffer.get(bytes);
                                log.info("ip:{},port:{} byte:{}",channel.socket().getInetAddress().getHostAddress(),channel.socket().getPort(),new String(bytes));
                            }else{
                                //log.info("无数据");
                            }
                        } catch (IOException e) {
                            log.error("数据读取异常====>",e);
                        }
                    });

                }

            }
        });

    }
}
