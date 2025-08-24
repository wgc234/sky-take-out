package com.wgc.interceptor;

import com.wgc.constant.JwtClaimsConstant;
import com.wgc.constant.MessageConstant;
import com.wgc.context.BaseContext;
import com.wgc.exception.FieldEmptyException;
import com.wgc.properties.JwtProperties;
import com.wgc.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
@Slf4j
public class JwtAdminInterceptor implements HandlerInterceptor {

    private final JwtProperties jwtProperties;

    private JwtAdminInterceptor(JwtProperties jwtProperties){
        this.jwtProperties = jwtProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断是否为动态方法请求
        if(!(handler instanceof HandlerMethod)){
            log.info("非动态请求，直接放行");
            return true;
        }

        //从请求头里面获取token
        String token = request.getHeader(jwtProperties.getAdminTokenName());

        // 预先验证 token 格式
        if (token == null || token.trim().isEmpty() || token.split("\\.").length != 3) {
            log.warn("无效的 JWT token 格式");
            response.setStatus(401);
            return false;
        }

        //校验令牌
        try {
            Claims claims = JwtUtils.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
            BaseContext.setCurrentId(empId);
            return true;
        } catch (Exception e) {
            log.error("令牌校验失败",e);
            response.setStatus(401);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清理ThreadLocal，防止内存泄漏
        BaseContext.removeCurrentId();
    }
}
