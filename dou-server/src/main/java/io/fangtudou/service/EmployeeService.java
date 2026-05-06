package io.fangtudou.service;

import io.fangtudou.dto.EmployeeDTO;
import io.fangtudou.dto.EmployeeLoginDTO;
import io.fangtudou.dto.EmployeePageQueryDTO;
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
}
