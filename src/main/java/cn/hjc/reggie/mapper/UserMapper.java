package cn.hjc.reggie.mapper;

import cn.hjc.reggie.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * 根据手机号获取用户信息
     * @param phone     用户手机号
     * @return          用户 | null
     */
    @Select("select * from user where phone = #{phone}")
    User getUserByPhone(String phone);

    /**
     * 添加用户到数据库
     * @param user
     * @return
     */
    @Insert("insert into user (id, phone) values (#{id}, #{phone})")
    Integer save(User user);

}
