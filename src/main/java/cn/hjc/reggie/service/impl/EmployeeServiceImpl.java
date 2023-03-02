package cn.hjc.reggie.service.impl;

import cn.hjc.reggie.domain.Page;
import cn.hjc.reggie.domain.Employee;
import cn.hjc.reggie.mapper.EmployeeMapper;
import cn.hjc.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public Employee findByUsernameAndPassword(Employee employee) {
        if (employee == null) {
            return null;
        }
        String username = employee.getUsername();
        String password = employee.getPassword();
        if (username == null || username.length() == 0 || password == null || password.length() == 0) {
            return null;
        }

        //进行md5加密处理
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //查询用户
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername, username);
        Employee newEmployee = employeeMapper.selectOne(wrapper);

        //若查询为null或密码错误则返回null
        if (newEmployee == null || !password.equals(newEmployee.getPassword())) {
            return null;
        }

        return newEmployee;
    }

    @Override
    public boolean createEmp(Employee employee) {
        //设置初始密码为123456,并进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8)));

        int insert = employeeMapper.insert(employee);

        return insert > 0;
    }

    @Override
    public Page<Employee> getEmployeePage(Integer current, Integer pageSize, String name, String phone) {

        //根据条件获取总记录数
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null && !"".equals(name), Employee::getName, name);
        queryWrapper.like(phone != null && !"".equals(phone), Employee::getPhone, phone);
        Integer total = employeeMapper.selectCount(queryWrapper);

        //如数据库中没有记录,则直接返回
        if (total < 1) {
            return new Page<Employee>(total, current, pageSize, null);
        }

        //若页码为空或小于1,默认改为1
        if (current == null || current < 1) current = 1;
        //每页数为空或小于1,默认改为3
        if (pageSize == null || pageSize < 1) pageSize = 3;
        //页码数大于最大页码数,则改为最大页码数
        int currentMax = total % pageSize == 0? total / pageSize: total / pageSize + 1;
        if (current > currentMax) current = currentMax;

        StringBuilder newName = new StringBuilder();
        if (name != null && !"".equals(name)) {
            newName.append("%").append(name).append("%");
            name = newName.toString();
        }
        StringBuilder newPhone = new StringBuilder();
        if (phone != null && !"".equals(phone)) {
            newPhone.append("%").append(phone).append("%");
            phone = newPhone.toString();
        }

        List<Employee> employees = employeeMapper.selectAllByPage((current - 1) * pageSize, pageSize, name, phone);

        return new Page<Employee>(total, current, pageSize, employees);
    }

    @Override
    public Employee findOneById(Long id) {
        if (id == null) {
            return null;
        }
        return employeeMapper.selectById(id);
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        int i = employeeMapper.updateById(employee);
        return i >= 1;
    }
}
