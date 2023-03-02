package cn.hjc.reggie.mapper;

import cn.hjc.reggie.domain.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * 获取菜品
     */
    @Select("select * from shopping_cart where dish_id = #{dishId} and user_id = #{userId}")
    ShoppingCart getDish(Long dishId, Long userId);

    /**
     * 获取套餐
     */
    @Select("select * from shopping_cart where setmeal_id = #{setmealId} and user_id = #{userId}")
    ShoppingCart getSetmeal(Long setmealId, Long userId);

    /**
     * 保存菜品到购物车
     */
    @Insert("insert into shopping_cart (id,  name, image, user_id, dish_id, dish_flavor, amount, create_time) " +
            "values (#{id}, #{name}, #{image}, #{userId}, #{dishId}, #{dishFlavor}, #{amount}, #{createTime})")
    Integer saveDish(ShoppingCart shoppingCart);

    /**
     * 保存套餐到购物车
     */
    @Insert("insert into shopping_cart (id,  name, image, user_id, setmeal_id, amount, create_time) " +
            "values (#{id}, #{name}, #{image}, #{userId}, #{setmealId}, #{amount}, #{createTime})")
    Integer saveSetmeal(ShoppingCart shoppingCart);

    /**
     * 根据菜品id删除该用户的购物车记录
     */
    @Delete("delete from shopping_cart where id = #{id}")
    Integer delOne(Long id);

    /**
     * 删除该用户的所有购物车信息
     */
    @Delete("delete from shopping_cart where user_id = #{userId}")
    Integer delByUserId(Long userId);

    /**
     * 将购物车数据加一
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    Integer upNumber(Integer number, Long id);

    /**
     * 获取所有购物车数据
     */
    @Select("select * from shopping_cart where user_id = #{userId}")
    List<ShoppingCart> getAll(Long userId);

}
