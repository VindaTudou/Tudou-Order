package io.fangtudou.service;

import io.fangtudou.dto.EmployeeLoginDTO;
import io.fangtudou.entity.Employee;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

}
