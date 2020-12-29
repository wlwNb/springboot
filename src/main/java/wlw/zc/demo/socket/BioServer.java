package wlw.zc.demo.socket;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;

@Component
@Slf4j
public class BioServer {
    @Resource
    private Executor executor;
    @PostConstruct
    public  void run() {
        int port = 9000;
        try {
            executor.execute(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    // 创建 bio socket服务
                    ServerSocket serverSocket = new ServerSocket();
                    // 绑定服务端口号
                    serverSocket.bind(new InetSocketAddress(port));
                    log.info("bio 准备运行端口 9000");
                    while (true) {
                        // 阻塞获得客户端socket
                        Socket socket = serverSocket.accept();
                        log.info("获取一个新链接 ip：{}，端口{}",socket.getInetAddress().getHostAddress(),socket.getPort());
                        // 进入自定义的handle方法,处理客户端的socket
                        new Thread(() ->{
                            byte[] bytes = new byte[1024];
                            while(true) {
                                try {
                                    // 获得输入流,读出客户端发送的数据
                                    int len = socket.getInputStream().read(bytes);
                                    if(len<0){

                                    }
                                    System.out.println(new String(bytes, 0, len));
                                    // 获得输出流,回写给客户端数据
                                    socket.getOutputStream().write("this is server".getBytes());
                                    socket.getOutputStream().flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }
            });
        }finally {

        }
    }

}
