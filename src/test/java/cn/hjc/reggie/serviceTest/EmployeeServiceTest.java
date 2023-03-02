package cn.hjc.reggie.serviceTest;

import cn.hjc.reggie.domain.Page;
import cn.hjc.reggie.domain.Employee;
import cn.hjc.reggie.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmployeeServiceTest {

    @Autowired
    EmployeeService employeeService;

    @Test
    public void pageTest() {
        Page<Employee> employeePage = employeeService.getEmployeePage(0, 0, " ", "130");
    }

}
