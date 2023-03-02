package cn.hjc.reggie.service;

import cn.hjc.reggie.domain.Category;
import cn.hjc.reggie.domain.Page;

import java.util.List;

public interface CategoryService {

    /**
     * 保存分类
     * @param category
     * @return
     */
    boolean save(Category category);

    /**
     * 查询分页数据
     * @param current   当前页码
     * @param pageSize  每页显示条数
     * @return
     */
    Page<Category> page(int current, int pageSize);

    /**
     * 根据id删除分类
     * @param id
     */
    void delete(Long id);

    /**
     * 根据id修改分类信息
     * @param category
     * @return
     */
    boolean update(Category category);

    /**
     * 根据类型查询所有分类
     * @param type 1.菜品  2.套餐
     * @return
     */
    List<Category> findByType(Integer type);

    /**
     * 查询所有分类
     * @return
     */
    List<Category> getAllList();

}
