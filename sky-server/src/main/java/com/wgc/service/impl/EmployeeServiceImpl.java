package com.wgc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wgc.context.MessageContext;
import com.wgc.dto.EmployeeLoginDTO;
import com.wgc.entity.Employee;
import com.wgc.enums.StatusEnum;
import com.wgc.exception.LoginFailedException;
import com.wgc.mapper.EmployeeMapper;
import com.wgc.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        //根据用户名查employee
        LambdaQueryWrapper<Employee> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Employee::getUsername,employeeLoginDTO.getUsername());
        Employee employee = employeeMapper.selectOne(lambdaQueryWrapper);

        if(employee==null){
            throw new LoginFailedException(MessageContext.USERNAME_NOT_EXIST);
        }
        String password = DigestUtils.md5DigestAsHex(employeeLoginDTO.getPassword().getBytes());
        if(!employee.getPassword().equals(password)){
            throw new LoginFailedException(MessageContext.PASSWORD_ERROR);
        }
        if(employee.getStatus().equals(StatusEnum.DISABLE.getCode())){
            throw new LoginFailedException(MessageContext.ACCOUNT_LOCKED);
        }

        return employee;
    }
}
