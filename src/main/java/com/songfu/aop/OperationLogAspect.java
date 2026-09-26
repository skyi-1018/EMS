package com.songfu.aop;

import com.songfu.annotation.OperationLog;
import com.songfu.common.UserContext;
import com.songfu.pojo.SysOperationLog;
import com.songfu.service.SysOperationLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Locale;

@Aspect
@Component
public class OperationLogAspect {

    @Autowired
    private SysOperationLogService sysOperationLogService;

    @Autowired
    private ObjectMapper objectMapper;

    @Around("@annotation(operationLog)")
    public Object around(ProceedingJoinPoint pjp, OperationLog operationLog) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            Object result = pjp.proceed();

            saveLog(pjp, operationLog, true, null, System.currentTimeMillis() - start);
            return result;
        } catch (Throwable e) {
            saveLog(pjp, operationLog, false, e, System.currentTimeMillis() - start);
            throw e;
        }
    }

    private void saveLog(ProceedingJoinPoint pjp, OperationLog operationLog, boolean success, Throwable e, long costTime) {
        try {
            SysOperationLog log = new SysOperationLog();

            log.setUserId(UserContext.getUserId());
            log.setOperation(operationLog.value());
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            log.setMethod(signature.getDeclaringTypeName() + "." + signature.getName());
            log.setParams(buildParams(pjp.getArgs()));
            log.setIp(getClientIp());
            log.setStatus(success ? 1 : 0);
            log.setErrorMsg(e == null ? null : truncate(e.getMessage(), 500));
            log.setCostTime(costTime);
            log.setCreateTime(LocalDateTime.now());

            sysOperationLogService.save(log);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String buildParams(Object[] args) {
        try {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < args.length; i++){
                Object arg = args[i];
                if (i > 0) sb.append(",");
                if (arg == null) {
                    sb.append("null");
                } else if (isSensitive(arg)) {
                    sb.append("\"***\"");
                } else {
                    sb.append(objectMapper.writeValueAsString(arg));
                }
            }
            return sb.append("]").toString();
        } catch(Exception ex) {
            return "参数序列化失败";
        }
    }

    private boolean isSensitive(Object arg) {
        String name = arg.getClass().getSimpleName().toLowerCase();
        String str = arg.toString().toLowerCase();
        return name.contains("password") || str.contains("password");
    }

    private String getClientIp() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) return null;

        HttpServletRequest request = attrs.getRequest();
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    private String truncate(String str, int max) {
        if (str == null) return null;
        return str.length() > max ? str.substring(0, max) : str;
    }
}
