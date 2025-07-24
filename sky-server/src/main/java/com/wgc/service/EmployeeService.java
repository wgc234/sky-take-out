package com.wgc.service;

import com.wgc.dto.EmployeeLoginDTO;
import com.wgc.entity.Employee;

public interface EmployeeService {
    Employee login(EmployeeLoginDTO employeeLoginDTO);
}
