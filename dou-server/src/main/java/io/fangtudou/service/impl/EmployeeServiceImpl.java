package io.fangtudou.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import java.util.List;
import io.fangtudou.constant.MessageConstant;
import io.fangtudou.constant.PasswordConstant;
import io.fangtudou.constant.StatusConstant;
import io.fangtudou.context.BaseContext;
import io.fangtudou.dto.EmployeeDTO;
import io.fangtudou.dto.EmployeeLoginDTO;
import io.fangtudou.dto.EmployeePageQueryDTO;
import io.fangtudou.entity.Employee;
import io.fangtudou.exception.AccountLockedException;
import io.fangtudou.exception.AccountNotFoundException;
import io.fangtudou.exception.PasswordErrorException;
import io.fangtudou.mapper.EmployeeMapper;
import io.fangtudou.result.PageResult;
import io.fangtudou.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @throws AccountNotFoundException
     * @throws PasswordErrorException
     * @throws AccountLockedException
     * @return employee
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //对前端的明文密码进行MD5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     * @param employeeDTO
     *
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        //对象属性拷贝
        BeanUtils.copyProperties(employeeDTO, employee);

        //设置对象状态
        employee.setStatus(StatusConstant.ENABLE);

        //设置默认密码
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        //设置创建时间
        employee.setCreateTime(LocalDateTime.now());

        //设置修改时间
        employee.setUpdateTime(LocalDateTime.now());

        //设置创建人id，修改人id
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.insert(employee);

    }

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return PageResult
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //分页查询,得到页数与页码
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);

        long total = page.getTotal();
        List<Employee> records = (List<Employee>) page.getResult();
        return new PageResult(total, records);
    }
    /**
     * 启用禁用员工账号
     * @param status
     * @param id
     *
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Employee employee = Employee.builder()
                        .id(id)
                        .status(status)
                        .updateTime(LocalDateTime.now()).build();
        employeeMapper.update(employee);
    }
}
