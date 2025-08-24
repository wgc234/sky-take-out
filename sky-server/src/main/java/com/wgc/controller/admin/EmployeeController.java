package com.wgc.controller.admin;

import com.wgc.constant.JwtClaimsConstant;
import com.wgc.constant.MessageConstant;
import com.wgc.context.BaseContext;
import com.wgc.dto.EmployeeAddDTO;
import com.wgc.dto.EmployeeLoginDTO;
import com.wgc.dto.EmployeeUpdateDTO;
import com.wgc.dto.EmployeeUpdatePasswordDTO;
import com.wgc.entity.Employee;
import com.wgc.properties.JwtProperties;
import com.wgc.result.PageResult;
import com.wgc.result.Result;
import com.wgc.service.EmployeeService;
import com.wgc.utils.JwtUtils;
import com.wgc.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 用户登录
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO){
        log.info("员工登录");
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

    /**
     * 退出登录
     * @return
     */
    @PostMapping("/logout")
    public Result logout(){
        log.info("退出登录");
        //清除ThreadLocal
        BaseContext.removeCurrentId();
        return Result.success();
    }

    /**
     * 新增员工
     * @param employeeAddDTO
     * @return
     */
    @PostMapping
    public Result save(@RequestBody EmployeeAddDTO employeeAddDTO){
        log.info("新增员工,{}",employeeAddDTO);
        employeeService.saveEmp(employeeAddDTO);

        return Result.success();
    }

    /**
     * 修改员工状态
     * @param status
     * @param employeeId
     * @return
     */
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable Integer status, Long employeeId){
        log.info("修改员工状态,{}",employeeId);

        employeeService.updateStatusById(employeeId,status);

        return Result.success();

    }

    @PutMapping("editPassword")
    public Result updatePassword(@RequestBody EmployeeUpdatePasswordDTO employeeUpdatePasswordDTO){
        log.info("修改员工密码");
        employeeService.updatePassword(employeeUpdatePasswordDTO);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody EmployeeUpdateDTO employeeUpdateDTO){
        log.info("修改员工信息");
        employeeService.updateEmp(employeeUpdateDTO);
        return Result.success();
    }

    /**
     * 分页查询员工
     */
    @GetMapping("/page")
    public PageResult<Employee> page(@RequestParam(name = "page", defaultValue = "1")Integer page,
                                     @RequestParam(name = "pageSize", defaultValue = "10")Integer pageSize,
                                     @RequestParam(name = "name", required = false)String name){
        log.info("分页查询员工,页码{},页大小{},员工名{}",page,pageSize,name);
        return employeeService.pageQuery(page, pageSize, name);
    }

    /**
     * 根据id查询员工
     */
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工,id{}",id);
        Employee employee = employeeService.getById(id);
        if (employee == null){
            return Result.error(MessageConstant.EMPLOYEE_NOT_FOUND);
        }
        return Result.success(employee);
    }

}
