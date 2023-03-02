package cn.hjc.reggie.controller;

import cn.hjc.reggie.common.R;
import cn.hjc.reggie.domain.Page;
import cn.hjc.reggie.dto.SetmealDto;
import cn.hjc.reggie.service.CategoryService;
import cn.hjc.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 添加新套餐
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        boolean flag = setmealService.save(setmealDto);
        if (flag) {
            return R.success("新增套餐成功~~");
        }
        return R.error("套餐新增失败,请稍后再试~~");
    }

    /**
     * 获取套餐分页数据
     */
    @GetMapping("/page")
    public R<Page> getPage(Integer page, Integer pageSize, String name) {
        Page<SetmealDto> setmealDtoPage = setmealService.getSetmealDtoPage(page, pageSize, name);
        return R.success(setmealDtoPage);
    }

    /**
     * 根据套餐id查询套餐信息
     */
    @GetMapping("/{id}")
    public R<SetmealDto> getSetmealDtoById(@PathVariable Long id) {
        SetmealDto setmealDto = setmealService.getById(id);
        return R.success(setmealDto);
    }

    /**
     * 修改套餐信息
     */
    @PutMapping
    public R<String> upSetmeal(@RequestBody SetmealDto setmealDto) {
        boolean flag = setmealService.update(setmealDto);
        if (flag) {
            return R.success("保存成功~~");
        }
        return R.error("保存失败,请稍后再试~~");
    }

    /**
     * 更改套餐状态
     */
    @PostMapping("/status/{status}")
    public R<String> upStatus(@PathVariable Integer status, Long[] ids) {
        boolean flag = setmealService.upStatus(status, ids);
        if (flag) {
            return R.success("操作成功~~");
        }
        return R.error("操作失败,请稍后再试~~");
    }

    /**
     * 删除套餐
     */
    @DeleteMapping
    public R<String> delSetmeal(Long[] ids) {
        boolean flag = setmealService.delete(ids);
        if (flag) {
            return R.success("删除成功");
        }
        return R.error("删除失败,请稍后重试~~");
    }

    /**
     * 获取套餐列表
     */
    @GetMapping("/list")
    public R<List<SetmealDto>> getList(Long categoryId, Integer status) {
        List<SetmealDto> setmealDtoList = setmealService.getSetmealList(categoryId, status);
        return R.success(setmealDtoList);
    }

}
