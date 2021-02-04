package wlw.zc.demo.aop;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import wlw.zc.demo.system.entity.YbyLog;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;

@Aspect
@Slf4j
public class OperationLogAop {

    // private  SysLogService sysLogService;

    @Pointcut("@annotation(wlw.zc.demo.aop.OperationLog)")
    public void pointcut() { }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) {
        Object result = null;
        long beginTime = System.currentTimeMillis();
        try {
            // 执行方法
            result = point.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        // 保存日志
        saveLog(point);
        return result;
    }

    private void saveLog(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        YbyLog log = new YbyLog();
        OperationLog logAnnotation = method.getAnnotation(OperationLog.class);
        if (logAnnotation != null) {
            // 注解上的描述
            log.setOperation(logAnnotation.operation());
            log.setMenu(logAnnotation.menu());
        }
        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.setMethod(className + "." + methodName + "()");
        // 请求的方法参数值
        Object[] args = joinPoint.getArgs();
        // 请求的方法参数名称
        String[] paramNames = signature.getParameterNames();
        if (args != null && paramNames != null) {
            StringBuilder params = new StringBuilder("{");
            for (int i = 0; i < args.length; i++) {
                params.append(paramNames[i]).append(": ").append(args[i]).append(",");
            }
            log.setParams(params.toString());
        }
        // 获取request
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        // 设置IP地址
        log.setIp(getIpAddr(request));
        //TODO 操作的变更字段前后数据从threadlocal中获取
        //log.setData(threadlocal.get());
        // TODO 从redis中获取
       /* log.setUsername("mrbird");
        log.setTime((int) time);
        log.setCreateTime(new Date()); */
         //TODO 保存系统日志
       // sysLogDao.saveSysLog(sysLog);

        //删掉缓存
        //threadlocal.remove();
    }

        /**
         * 获取IP地址
         *
         * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
         * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
         */
        private static  String getIpAddr(HttpServletRequest request) {

            String ip = request.getHeader("x-forwarded-for");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
        }
    }
