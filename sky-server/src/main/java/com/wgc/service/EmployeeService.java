package com.wgc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wgc.dto.EmployeeAddDTO;
import com.wgc.dto.EmployeeLoginDTO;
import com.wgc.dto.EmployeeUpdateDTO;
import com.wgc.dto.EmployeeUpdatePasswordDTO;
import com.wgc.entity.Employee;
import com.wgc.result.PageResult;

public interface EmployeeService extends IService<Employee> {
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void saveEmp(EmployeeAddDTO employeeAddDTO);

    void updateStatusById(Long employeeId, Integer status);

    void updatePassword(EmployeeUpdatePasswordDTO employeeUpdatePasswordDTO);


    void updateEmp(EmployeeUpdateDTO employeeUpdateDTO);

    PageResult<Employee> pageQuery(Integer page, Integer pageSize, String name);
}
