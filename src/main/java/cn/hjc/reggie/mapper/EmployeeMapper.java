package cn.hjc.reggie.mapper;

import cn.hjc.reggie.domain.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

    @Select({"<script>" +
            "select * from employee" +
                    "<where>" +
                            "<if test = 'name != null and name != \"\" '> name like #{name} </if>" +
                            "<if test = 'phone != null and phone != \"\" '> and phone like #{phone} </if>" +
                    "</where>" +
                    "limit #{current}, #{pageSize}" +
            "</script>"})
    List<Employee> selectAllByPage(Integer current, Integer pageSize, String name, String phone);

}