package cn.hjc.reggie.service;

import cn.hjc.reggie.domain.Dish;
import cn.hjc.reggie.domain.Page;
import cn.hjc.reggie.dto.DishDto;

import java.util.List;

public interface DishService {

    /**
     * 保存菜品
     * @param dishDto
     * @return  成功 || 失败
     */
    boolean save(DishDto dishDto);

    /**
     * 查询菜品分页信息
     * @param current          当前页码
     * @param pageSize      每页显示条数
     * @param name          数据模糊名皮匹配
     * @return
     */
    Page<DishDto> getDishPage(Integer current, Integer pageSize, String name);

    /**
     * 修改菜品的售卖状态
     * @param status    0.停售        1.起售
     * @param id       菜品id
     * @return
     */
    boolean updateStatus(String status, String id);

    /**
     * 根据id查询菜品和对应口味
     * @param id
     * @return
     */
    DishDto getDishAndFlavorById(Long id);

    /**
     * 修改菜品信息
     * @param dishDto
     * @return
     */
    boolean update(DishDto dishDto);

    /**
     * 删除菜品信息
     * @param id
     * @return
     */
    boolean del(Long[] id);

    /**
     * 根据分类id获取菜品
     * @param categoryId
     * @return
     */
    List<DishDto> getDishByCategoryId(Long categoryId);

}
