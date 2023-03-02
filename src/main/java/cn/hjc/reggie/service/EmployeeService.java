package cn.hjc.reggie.service;

import cn.hjc.reggie.domain.Page;
import cn.hjc.reggie.domain.Employee;

public interface EmployeeService {

    /**
     * 根据员工的用户名和密码查询用户
     * @param employee null || 用户对象
     * @return
     */
    Employee findByUsernameAndPassword(Employee employee);

    /**
     * 创建员工
     * @param employee
     * @return
     */
    boolean createEmp(Employee employee);

    /**
     * 根据条件查询分页对象
     * @param current   当前页数
     * @param pageSize  每页显示条数
     * @param name      名字
     * @param phone     手机号
     * @return
     */
    Page<Employee> getEmployeePage(Integer current, Integer pageSize, String name, String phone);

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    Employee findOneById(Long id);

    /**
     * 修改员工信息
     * @param employee
     * @return
     */
    boolean updateEmployee(Employee employee);

}
