package cn.hjc.reggie.controller;


import cn.hjc.reggie.common.BaseContext;
import cn.hjc.reggie.common.R;
import cn.hjc.reggie.domain.Dish;
import cn.hjc.reggie.domain.Page;
import cn.hjc.reggie.dto.DishDto;
import cn.hjc.reggie.service.DishFlavorService;
import cn.hjc.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 菜品
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto, HttpServletRequest request) {
        BaseContext.setCurrentId((long) request.getSession().getAttribute("employee"));
        if (!dishService.save(dishDto)) {
            return R.error("保存失败");
        }
        return R.success("保存成功");

    }

    /**
     * 获取菜品分页数据集
     * @param page          当前页码
     * @param pageSize      每页显示数
     * @param name          模糊匹配菜品名
     * @return
     */
    @GetMapping("/page")
    public R<Page> getPage(Integer page, Integer pageSize, String name) {

        Page<DishDto> dishPage = dishService.getDishPage(page, pageSize, name);
        if (dishPage == null) {
            return R.error("数据查询失败~~");
        }
        return R.success(dishPage);
    }

    /**
     * 根据id修改菜品数据
//     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable String status, String ids) {
        boolean flag = dishService.updateStatus(status, ids);
        if (flag) {
            return R.success("修改成功~~");
        }
        return R.error("修改失败~~");
    }

    /**
     * 查询菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id) {
        DishDto dishDto = dishService.getDishAndFlavorById(id);
        if (dishDto == null) {
            return R.error("服务器繁忙，请稍后再试~~");
        }
        return R.success(dishDto);
    }

    /**
     * 更新菜品
     * @param dishDto
     * @param request
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto, HttpServletRequest request) {
        BaseContext.setCurrentId((Long) request.getSession().getAttribute("employee"));
        boolean flag = dishService.update(dishDto);
        if (flag) {
            return R.success("修改成功~");
        }
        return R.error("修改失败~");
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delDish(String ids) {
        String[] id = ids.split(",");
        Long[] newIds = new Long[id.length];

        for (int i = 0; i < id.length; i++) {
            newIds[i] = Long.parseLong(id[i]);
        }

        boolean del = dishService.del(newIds);
        if (del) {
            return R.success("删除菜品成功~~");
        }
        return R.error("删除菜品失败~~");
    }

    /**
     * 根据菜品分类id获取菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public R<List> getDishByCategoryId(Long categoryId) {
        List<DishDto> dishDtoList = dishService.getDishByCategoryId(categoryId);

        if (dishDtoList == null) {
            return R.error("服务器繁忙,请稍后再试~~");
        }
        return R.success(dishDtoList);
    }

}
