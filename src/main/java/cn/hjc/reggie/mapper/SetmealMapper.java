package cn.hjc.reggie.mapper;

import cn.hjc.reggie.domain.Setmeal;
import cn.hjc.reggie.domain.SetmealDish;
import cn.hjc.reggie.dto.SetmealDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {

    /**
     * 插入一条套餐数据
     * @param setmealDto
     * @return
     */
    @Insert("insert into setmeal (id, category_id, name, price, status, description, image, " +
            "create_time, update_time, create_user, update_user) " +
            "values (#{id}, #{categoryId}, #{name}, #{price}, #{status}, #{description}, #{image}," +
            " #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    Integer insertSetmeal(SetmealDto setmealDto);

    /**
     * 保存套餐内关联的菜品
     */
    @Insert("<script>" +
            "insert into setmeal_dish (id, setmeal_id, dish_id, name, price, copies, " +
            "create_time, update_time, create_user, update_user) values " +
            "<foreach collection = 'list' item = 'item' separator = ','>" +
            "(#{item.id}, #{item.setmealId}, #{item.dishId}, #{item.name}, #{item.price}, #{item.copies}," +
            "#{item.createTime}, #{item.updateTime}, #{item.createUser}, #{item.updateUser})" +
            "</foreach>" +
            "" +
            "" +
            "</script>")
    Integer insertSetmealDish(List<SetmealDish> list);

    /**
     * 获取总数
     */
    @Select("select count(*) from setmeal where name like #{name}")
    Integer getTotal(String name);

    /**
     * 获取套餐分页数据
     */
    @Select("select * from setmeal where name like #{name} limit #{current}, #{pageSize} ")
    List<Setmeal> getAllPage(Integer current, Integer pageSize, String name);

    /**
     * 根据id获取套餐
     */
    @Select("select * from setmeal where id = #{id}")
    SetmealDto getById(Long id);

    /**
     * 根据套餐id查询菜品
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getDishBySetmealId(Long setmealId);

    /**
     * 更新套餐信息
     */
    @Update("update setmeal set category_id=#{categoryId}, name=#{name}, price=#{price}, status=#{status}, " +
            "description=#{description}, image=#{image}, update_time=#{updateTime}, update_user=#{updateUser} " +
            " where id = #{id}")
    Integer upSetmeal(SetmealDto setmealDto);

    /**
     * 更新套餐所关联的菜品
     */
    @Update("update setmeal_dish " +
            "set name = #{name}, price = #{price}, copies = #{copies}, " +
            "update_time = #{updateTime}, update_user = #{updateUser} " +
            "where id = #{id}")
    Integer upSetmealDish(SetmealDish setmealDish);

    /**
     * 删除套餐内所有菜品
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    Integer delDish(Long setmealId);

    @Delete("delete from setmeal where id = #{id}")
    Integer del(Long id);

    /**
     * 根据id修改售卖状态
     */
    @Update("update setmeal set status = #{status} where id = #{id}")
    Integer upStatus(Integer status, Long id);

    /**
     * 根据分类id和状态获取套餐
     */
    @Select("select * from setmeal where category_id = #{categoryId} and status = #{status}")
    List<Setmeal> getByCategoryIdAndStatus(Long categoryId, Integer status);

}
