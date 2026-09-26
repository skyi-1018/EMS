package com.songfu.interceptor;

import com.songfu.annotation.RequireRole;
import com.songfu.common.UserContext;
import com.songfu.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JWTUtil jwtUtil;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.从请求头中取token
        String header = request.getHeader("Authorization");

        // 2.判断token是否存在
        if (header == null || !header.startsWith("Bearer ")) {
            log.info("token不存在");
            writeUnauthorized(response, "未登录");
            return false;
        }
        String token = header.substring(7);

        // 3.如果token存在，校验令牌，如果验证失败也报错
        try {
            Claims claims = jwtUtil.parseToken(token);
            Integer id = Integer.valueOf(claims.getSubject());
            String username = claims.get("username", String.class);
            Integer role = claims.get("role", Integer.class);

            UserContext.set(id, username, role);
        } catch (Exception e) {
            log.info("token校验失败：{}", e.toString());
            writeUnauthorized(response, "登录已过期，请重新登录");
            return false;
        }

        // 4.角色校验
        if (handler instanceof HandlerMethod handlerMethod) {
            RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);
            if (requireRole == null) {
                requireRole = handlerMethod.getBeanType().getAnnotation(RequireRole.class);
            }

            if (requireRole != null) {
                Integer role = UserContext.getRole();
                boolean allowed = Arrays.stream(requireRole.value())
                        .anyMatch(r -> r == role);
                if (!allowed) {
                    log.info("无权限访问");
                    writeForbidden(response);
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        // 最后要移除用户信息
        UserContext.remove();
    }

    private void writeUnauthorized(HttpServletResponse response, String msg) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":401,\"msg\":\"" + msg + "\",\"data\":null}");
    }

    private void writeForbidden(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":403,\"msg\":\"无权限访问\",\"data\":null}");
    }
}
