package io.fangtudou.service;

import io.fangtudou.dto.EmployeeDTO;
import io.fangtudou.dto.EmployeeLoginDTO;
import io.fangtudou.dto.EmployeePageQueryDTO;
import io.fangtudou.dto.PasswordEditDTO;
import io.fangtudou.entity.Employee;
import io.fangtudou.result.PageResult;

import java.awt.*;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return employee
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用或禁用员工
     * @param status
     * @param id
     *
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据id查询员工
     * @param id
     * @return Result
     */
    Employee getById(Long id);

    /**
     * 修改员工信息
     * @param employeeDTO
     */
    void update(EmployeeDTO employeeDTO);

    /**
     * 修改密码
     * @param passwordEditDTO
     */
    void editPassword(PasswordEditDTO passwordEditDTO);
}
