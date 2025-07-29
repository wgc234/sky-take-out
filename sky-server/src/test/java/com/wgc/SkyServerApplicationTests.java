package com.wgc;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class SkyServerApplicationTests {
    private final String token="eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3NTM4NDUzOTcsInVzZXJJZCI6MTIzfQ.emCq60q-qi3W_aWhfwFFIQMBGSNL4XFoU3yaIJD2Hww";

    private final String secretKey="wgc";
    @Test
    public void jwtCreate(){
        //定义密钥

        //定义截止日期
        Date exp=new Date(System.currentTimeMillis()+1000*60*60*24);
        //定义内容
        Map<String,Object> claims=new HashMap<>();
        claims.put("userId",123);
        //定义签名算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //生成token
        String token = Jwts.builder()
                .setClaims(claims)
                .setExpiration(exp)
                .signWith(signatureAlgorithm, secretKey.getBytes(StandardCharsets.UTF_8))
                .compact();

        System.out.println(token);
    }

    @Test
    public void parseJWT(){
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token).getBody();
        System.out.println(claims.get("userId"));
    }
}
