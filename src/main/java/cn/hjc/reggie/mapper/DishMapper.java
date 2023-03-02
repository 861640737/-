package cn.hjc.reggie.mapper;

import cn.hjc.reggie.domain.Dish;
import cn.hjc.reggie.dto.DishDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

    /**
     * 新增菜品
     * id, name, category_id, price, code, image, description, create_time, update_time, create_user, update_user
     * id  名字     类别id      价格   商品码  图片     简介          创建时间      修改时间       创建用户       修改用户
     *
     */
    @Insert("insert into dish(" +
            "id, name, category_id, price, code, image, description, " +
            "create_time, update_time, create_user, update_user) " +
            " values(#{id}, #{name}, #{categoryId}, #{price}, #{code}, #{image}, " +
            " #{description}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    Integer insert(DishDto dishDto);

    /**
     * 根据名字模糊匹配分页数据
     * @param current   起始点
     * @param pageSize  每页显示条数
     * @param name      模糊匹配名
     * @return
     */
    @Select("<script>" +
                "select * from dish " +
                "<where>" +
                    "<if test = 'name != null'> name like #{name} </if>" +
                "</where>" +
                " order by create_time asc " +
                " limit #{current}, #{pageSize} " +
            "</script>")
    List<Dish> selectAllByPage(Integer current, Integer pageSize, String name);


    /**
     * 查询模糊匹配的菜品总数
     * @param name  模糊匹配字段
     * @return
     */
    @Select("<script>" +
                   "select count(*) from dish " +
                   "<where>" +
                        "<if test = 'name != null'> name like #{name} </if>" +
                   "</where>" +
           "</script>")
    Integer getTotal (String name);

    /**
     * 根据id修改售卖状态
     * @param status
     * @param id
     * @return
     */
    @Update("update dish set status = #{status} where id = #{id}")
    Integer upStatus(Integer status, Long id);

    /**
     * 根据id查询菜品信息
     * @param id
     * @return
     */
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    /**
     * 修改菜品信息
     * @param dishDto
     * @return
     */
    @Update("update dish set name = #{name}, category_id = #{categoryId}, price = #{price}, image = #{image}, " +
            " description = #{description}, update_time = #{updateTime}, update_user = #{updateUser} " +
            " where id = #{id}")
    Integer update(DishDto dishDto);

    /**
     * 根据菜品id删除对应菜品
     * @param id
     * @return
     */
    @Delete("delete from dish where id = #{id}")
    Integer delById(Long id);

    /**
     * 根据菜品分类查询菜品
     * @param id    对应菜品的分类id
     * @return
     */
    @Select("select * from dish where category_id = #{id} and status = 1")
    List<Dish> getDishByCategoryId(Long id);

}
