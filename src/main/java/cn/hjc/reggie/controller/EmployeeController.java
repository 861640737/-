package cn.hjc.reggie.controller;

import cn.hjc.reggie.common.BaseContext;
import cn.hjc.reggie.domain.Page;
import cn.hjc.reggie.common.R;
import cn.hjc.reggie.domain.Employee;
import cn.hjc.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 员工
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee  包含用户账号和密码
     * @return
     */
    @PostMapping({"/login"})
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        Employee emp = employeeService.findByUsernameAndPassword(employee);

        //判断是否登入成功
        if (emp == null) {
            return R.error("用户名或密码错误,请重试~");
        }

        //判断账号是否锁定
        if (emp.getStatus() == 0) {
            return R.error("该账号已禁用~");
        }

        //登入成功，将员工id存入session,并返回登入成功结果
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出,清除session中的id
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        long empId = (long) request.getSession().getAttribute("employee");
        BaseContext.setCurrentId(empId);

        boolean flag = employeeService.createEmp(employee);

        if (flag) {
            log.info("新增员工:{}--{}", employee.getUsername(), employee.getName());
            return R.success("添加员工成功");
        }
        return R.error("添加员工失败");
    }

    /**
     * 根据条件返回分页数据
     * @param page      当前页码
     * @param pageSize  每页显示数
     * @param name      模糊匹配员工姓名
     * @param phone     模糊匹配员工手机号
     * @return
     */
    @GetMapping("/page")
    public R<Page<Employee>> page(Integer page, Integer pageSize, String name, String phone) {
        Page<Employee> employeePage = employeeService.getEmployeePage(page, pageSize, name, phone);
        if (employeePage != null) {
            return R.success(employeePage);
        }
        log.error("分页查询异常");
        return R.error("服务器开小差了,请稍后再试~");
    }

    /**
     * 修改员工信息
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        if (employee == null) {
            return null;
        }
        //获取需要修改的员工信息
        Employee employee1 = employeeService.findOneById(employee.getId());
        if (employee1 == null) {
            return R.error("该员工不存在");
        }

        //获取当前操作人的id
        Long empId = (Long) request.getSession().getAttribute("employee");
        BaseContext.setCurrentId(empId);
        //非管理员修改账号状态,则返回权限不够
        if (!employee.getStatus().equals(employee1.getStatus()) && empId != 1) {
            R.error("您的权限不支持修改状态");
        }

        boolean b = employeeService.updateEmployee(employee);
        if (b) {
            R.success("修改成功");
        }
        return R.success("修改失败");
    }

    /**
     * 根据id获取员工
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getEmployee(@PathVariable Long id) {
        Employee oneById = employeeService.findOneById(id);
        if (oneById == null) {
            return R.error("该员工不存在");
        }
        return R.success(oneById);

    }

}
