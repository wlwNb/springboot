package wlw.zc.demo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import wlw.zc.demo.service.BaseService;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

import static io.netty.handler.codec.stomp.StompHeaders.HEART_BEAT;

@Slf4j
@Component
public class NettyServerHandler extends SimpleChannelInboundHandler<Object> {
    @Resource
    private BaseService baseService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            System.out.println(msg.toString());
            //ByteBuf buf  = (ByteBuf) msg;
            //System.out.println(buf .toString(CharsetUtil.UTF_8));
//            SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//            String callback = sf.format(new Date());
//            ctx.write(Unpooled.copiedBuffer(callback.getBytes()));
            //这里调用service服务
            //baseService.send();
        }  finally {
            //ReferenceCountUtil.release(msg);
        }
    }
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                log.info("已经5秒没有收到信息！");
                //向客户端发送消息
                ctx.writeAndFlush(HEART_BEAT).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        }
        super.userEventTriggered(ctx, evt);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 出现异常就关闭
        cause.printStackTrace();
        ctx.close();
    }
}
