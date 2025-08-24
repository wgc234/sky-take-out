package com.wgc.utils;

import com.wgc.constant.MessageConstant;
import com.wgc.exception.LoginFailedException;
import io.jsonwebtoken.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtils {

    public static String createJWT(String secretKey, Long ttlMillis, Map<String,Object> claims){
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setExpiration(exp)
                .signWith(signatureAlgorithm,secretKey.getBytes(StandardCharsets.UTF_8));
        return builder.compact();
    }


    public static Claims parseJWT(String secretKey,String token){
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token).getBody();
        } catch (JwtException | IllegalArgumentException e) {
            // JwtException 捕获了所有 jjwt 相关的解析错误
            // IllegalArgumentException 捕获了 token 为空或空白等问题
            // 可以在这里记录日志 log.error("Token 解析失败", e);
            throw new LoginFailedException(MessageConstant.TOKEN_ERROR);
        }
    }
}

