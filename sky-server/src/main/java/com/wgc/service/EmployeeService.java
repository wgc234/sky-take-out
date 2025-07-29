package com.wgc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wgc.dto.EmployeeAddDTO;
import com.wgc.dto.EmployeeLoginDTO;
import com.wgc.entity.Employee;

public interface EmployeeService extends IService<Employee> {
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void saveEmp(EmployeeAddDTO employeeAddDTO);
}
