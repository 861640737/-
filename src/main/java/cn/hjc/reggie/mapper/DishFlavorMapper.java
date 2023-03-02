package cn.hjc.reggie.mapper;

import cn.hjc.reggie.domain.DishFlavor;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 保存菜品口味
     * @param lists
     * @return
     */
    @Insert("<script>" +
            "insert into dish_flavor(" +
            "id, dish_id, name, value, create_time, update_time, create_user, update_user) values " +
            "<foreach collection='lists' item='item' separator=',' > " +
            "(#{item.id}, #{item.dishId}, #{item.name}, #{item.value}, #{item.createTime}, #{item.updateTime}, #{item.createUser}, #{item.updateUser}) " +
            "</foreach>" +
            "</script>")
    Integer insert(List<DishFlavor> lists);

    /**
     * 根据菜品id查询所有口味
     * @param DishId
     * @return
     */
    @Select("select * from dish_flavor where dish_id = #{DishId}")
    List<DishFlavor> getFlavorByDishId(Long DishId);

    /**
     * 根据菜品id删除口味
     * @param id
     * @return
     */
    @Delete("delete from dish_flavor where dish_id = #{id}")
    Integer delByDishId(Long id);

}
