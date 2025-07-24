package com.wgc.controller.admin;

import com.wgc.context.JwtClaimsConstant;
import com.wgc.dto.EmployeeLoginDTO;
import com.wgc.entity.Employee;
import com.wgc.properties.JwtProperties;
import com.wgc.result.Result;
import com.wgc.service.EmployeeService;
import com.wgc.utils.JwtUtils;
import com.wgc.vo.EmployeeLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO){
        //调用 servic层的login方法，返回用户实体类
        Employee employee=employeeService.login(employeeLoginDTO);

        Map<String,Object> claims=new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID,employee.getId());
        //生成对应的token
        String token= JwtUtils.createJWT(jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .userName(employee.getUsername())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

}
