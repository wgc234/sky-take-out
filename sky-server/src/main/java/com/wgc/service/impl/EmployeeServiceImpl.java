package com.wgc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wgc.constant.MessageConstant;
import com.wgc.constant.PasswordConstant;
import com.wgc.constant.StatusConstant;
import com.wgc.dto.EmployeeAddDTO;
import com.wgc.dto.EmployeeLoginDTO;
import com.wgc.dto.EmployeeUpdateDTO;
import com.wgc.dto.EmployeeUpdatePasswordDTO;
import com.wgc.entity.Employee;
import com.wgc.enums.StatusEnum;
import com.wgc.exception.FieldEmptyException;
import com.wgc.exception.LoginFailedException;
import com.wgc.mapper.EmployeeMapper;
import com.wgc.result.PageResult;
import com.wgc.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        //根据用户名查employee
        LambdaQueryWrapper<Employee> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Employee::getUsername,employeeLoginDTO.getUsername());
        Employee employee = employeeMapper.selectOne(lambdaQueryWrapper);

        if(employee==null){
            throw new LoginFailedException(MessageConstant.USERNAME_NOT_EXIST);
        }
        String password = DigestUtils.md5DigestAsHex(employeeLoginDTO.getPassword().getBytes());
        if(!employee.getPassword().equals(password)){
            throw new LoginFailedException(MessageConstant.PASSWORD_ERROR);
        }
        if(employee.getStatus().equals(StatusEnum.DISABLE.getCode())){
            throw new LoginFailedException(MessageConstant.ACCOUNT_LOCKED);
        }

        return employee;
    }

    @Override
    public void saveEmp(EmployeeAddDTO employeeAddDTO) {
        //判断字段是否为空
        if (employeeAddDTO.getUsername() == null || employeeAddDTO.getUsername().trim().isEmpty()) {
            throw new FieldEmptyException(MessageConstant.USERNAME_CANNOT_BE_EMPTY);
        }
        if (employeeAddDTO.getName() == null || employeeAddDTO.getName().trim().isEmpty()) {
            throw new FieldEmptyException(MessageConstant.NAME_CANNOT_BE_EMPTY);
        }
        if (employeeAddDTO.getPhone() == null || employeeAddDTO.getPhone().trim().isEmpty()) {
            throw new FieldEmptyException(MessageConstant.PHONE_CANNOT_BE_EMPTY);
        }
        if (employeeAddDTO.getSex() == null || employeeAddDTO.getSex().trim().isEmpty()) {
            throw new FieldEmptyException(MessageConstant.SEX_CANNOT_BE_EMPTY);
        }
        if (employeeAddDTO.getIdNumber() == null || employeeAddDTO.getIdNumber().trim().isEmpty()) {
            throw new FieldEmptyException(MessageConstant.ID_NUMBER_CANNOT_BE_EMPTY);
        }
        String username = employeeAddDTO.getUsername().trim();
        if (username.length() < 3 || username.length() > 20) {
            throw new FieldEmptyException(MessageConstant.USERNAME_FORMAT_ERROR);
        }
        if (!username.matches("^[a-zA-Z0-9]+$")) {
            throw new FieldEmptyException(MessageConstant.USERNAME_FORMAT_ERROR);
        }
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Employee::getUsername, username);
        Employee existEmployee = employeeMapper.selectOne(lambdaQueryWrapper);
        if (existEmployee != null) {
            throw new FieldEmptyException(MessageConstant.USERNAME_DUPLICATE);
        }

        // 员工姓名校验
        String name = employeeAddDTO.getName().trim();
        if (name.isEmpty()|| name.length() > 12) {
            throw new FieldEmptyException(MessageConstant.NAME_LENGTH_ERROR);
        }
        if (!name.matches("^[\\u4e00-\\u9fa5a-zA-Z]+$")) {
            throw new FieldEmptyException(MessageConstant.NAME_FORMAT_ERROR);
        }

        // 手机号校验
        String phone = employeeAddDTO.getPhone().trim();
        if (!phone.matches("^1[3-9]\\d{9}$")) {
            throw new FieldEmptyException(MessageConstant.PHONE_FORMAT_ERROR);
        }

        // 身份证号校验
        String idNumber = employeeAddDTO.getIdNumber().trim();
        if (idNumber.length() != 18 || !isValidIdNumber(idNumber)) {
            throw new FieldEmptyException(MessageConstant.ID_NUMBER_FORMAT_ERROR);
        }

        //新增员工
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeAddDTO,employee);
        employee.setStatus(StatusConstant.ENABLE);
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        employeeMapper.insert(employee);
    }

    @Override
    public void updateStatusById(Long employeeId, Integer status) {
        //根据id修改状态
        lambdaUpdate().eq(Employee::getId,employeeId)
                .set(Employee::getStatus,status)
                .update();
    }

    @Override
    public void updatePassword(EmployeeUpdatePasswordDTO employeeUpdatePasswordDTO) {
        //根据员工id查询员工信息
        Employee employee = employeeMapper.selectById(employeeUpdatePasswordDTO.getEmpId());
        if(employee==null){
            throw new LoginFailedException(MessageConstant.EMPLOYEE_NOT_FOUND);
        }
        //校验旧密码是否正确
        if (!employee.getPassword().equals(DigestUtils.md5DigestAsHex(employeeUpdatePasswordDTO.getOldPassword().getBytes()))) {
            throw new LoginFailedException(MessageConstant.PASSWORD_ERROR);
        }
        //更新员工密码
        employee.setPassword(DigestUtils.md5DigestAsHex(employeeUpdatePasswordDTO.getNewPassword().getBytes()));
        employeeMapper.updateById(employee);
    }

    @Override
    public void updateEmp(EmployeeUpdateDTO employeeUpdateDTO) {
        if(employeeUpdateDTO==null){
            throw new FieldEmptyException(MessageConstant.ID_CANNOT_BE_EMPTY);
        }
        //校验是否为空
        if (employeeUpdateDTO.getId() == null) {
            throw new FieldEmptyException(MessageConstant.ID_CANNOT_BE_EMPTY);
        }
        if (employeeUpdateDTO.getName() == null || employeeUpdateDTO.getName().trim().isEmpty()){
            throw new FieldEmptyException(MessageConstant.NAME_CANNOT_BE_EMPTY);
        }
        if (employeeUpdateDTO.getPhone() == null || employeeUpdateDTO.getPhone().trim().isEmpty()) {
            throw new FieldEmptyException(MessageConstant.PHONE_CANNOT_BE_EMPTY);
        }
        if (employeeUpdateDTO.getSex() == null || employeeUpdateDTO.getSex().trim().isEmpty()) {
            throw new FieldEmptyException(MessageConstant.SEX_CANNOT_BE_EMPTY);
        }
        if (employeeUpdateDTO.getIdNumber() == null || employeeUpdateDTO.getIdNumber().trim().isEmpty()) {
            throw new FieldEmptyException(MessageConstant.ID_NUMBER_CANNOT_BE_EMPTY);
        }
        //查询该用户是否存在
        Employee employee = employeeMapper.selectById(employeeUpdateDTO.getId());
        if (employee == null) {
            throw new FieldEmptyException(MessageConstant.EMPLOYEE_NOT_FOUND);
        }
        Employee newEmployee = new Employee();
        BeanUtils.copyProperties(employeeUpdateDTO,newEmployee);
        employeeMapper.updateById(newEmployee);
    }

    @Override
    public PageResult<Employee> pageQuery(Integer page, Integer pageSize, String name) {
        //校验页码和页数
        if (page <= 0 || pageSize <= 0) {
            throw new FieldEmptyException(MessageConstant.PAGE_AND_PAGE_SIZE_CANNOT_BE_EMPTY);
        }
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        //构建根据姓名查询的查询器
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.hasText(name),Employee::getName,name);

        //调用分页查询方法
        employeeMapper.selectPage(pageInfo, lambdaQueryWrapper);
        return new PageResult<Employee>(pageInfo.getTotal(),pageInfo.getRecords());
    }

    private boolean isValidIdNumber(String idNumber) {
        // 基本格式检查
        if (idNumber == null || idNumber.length() != 18) {
            return false;
        }

        // 检查前17位是否都是数字
        if (!idNumber.substring(0, 17).matches("\\d{17}")) {
            return false;
        }

        // 检查最后一位是否是数字或X
        char lastChar = idNumber.charAt(17);
        if (!(Character.isDigit(lastChar) || lastChar == 'X' || lastChar == 'x')) {
            return false;
        }

        // 校验码验证
        int[] weights = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] checks = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += (idNumber.charAt(i) - '0') * weights[i];
        }

        int remainder = sum % 11;
        char checkChar = checks[remainder];

        return Character.toUpperCase(lastChar) == checkChar;
    }
}
