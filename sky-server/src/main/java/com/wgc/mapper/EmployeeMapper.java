package com.wgc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wgc.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
