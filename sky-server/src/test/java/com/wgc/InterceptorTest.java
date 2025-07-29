package com.wgc;

import com.wgc.constant.JwtClaimsConstant;
import com.wgc.properties.JwtProperties;
import com.wgc.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.AdditionalMatchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class InterceptorTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtProperties jwtProperties;

    private String validToken;

    @BeforeEach
    public void setUp() {
        // 创建一个有效的Token以供测试使用
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, 1L); // 假设员工ID为1
        validToken = JwtUtils.createJWT(jwtProperties.getAdminSecretKey(), jwtProperties.getAdminTtl(), claims);
    }

    @Test
    public void testInterceptor_withValidToken() throws Exception {
        // 场景: 请求受保护的路径并提供有效的Token
        // 期望: 请求成功 (200 OK)
        mockMvc.perform(get("/admin/test")
                        .header(jwtProperties.getAdminTokenName(), validToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testInterceptor_withoutToken() throws Exception {
        // 场景: 请求受保护的路径但没有提供Token
        // 期望: 请求被拦截 (401 Unauthorized)
        mockMvc.perform(get("/admin/test"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testInterceptor_withInvalidToken() throws Exception {
        // 场景: 请求受保护的路径但提供了无效的Token
        // 期望: 请求被拦截 (401 Unauthorized)
        String invalidToken = "this-is-an-invalid-token";
        mockMvc.perform(get("/admin/test")
                        .header(jwtProperties.getAdminTokenName(), invalidToken))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    public void testInterceptor_withExpiredToken() throws Exception {
        // 场景: 请求受保护的路径但提供了已过期的Token
        // 期望: 请求被拦截 (401 Unauthorized)
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, 2L);
        // 创建一个立即过期的token
        String expiredToken = JwtUtils.createJWT(jwtProperties.getAdminSecretKey(), -1L, claims);
        
        mockMvc.perform(get("/admin/test")
                        .header(jwtProperties.getAdminTokenName(), expiredToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testInterceptor_excludedPath() throws Exception {
        // 1. 先执行请求，并把结果存到 MvcResult 对象中
        MvcResult result = mockMvc.perform(get("/admin/employee/login"))
                .andReturn(); // 使用 andReturn() 来获取结果

        // 2. 从结果中获取响应对象，再获取状态码
        int statusCode = result.getResponse().getStatus();

        // 3. 使用标准的 JUnit 5 断言来判断
        assertNotEquals(401, statusCode, "拦截器放行了登录路径，状态码不应为401");
    }
} 