package cn.hjc.reggie.service;

import cn.hjc.reggie.domain.Page;
import cn.hjc.reggie.dto.SetmealDto;

import java.util.List;

public interface SetmealService {

    /**
     * 新增套餐
     */
    boolean save(SetmealDto setmealDto);

    /**
     * 获取套餐分页数据
     */
    Page<SetmealDto> getSetmealDtoPage(Integer current, Integer pageSize, String name);

    /**
     * 根据id获取套餐数据
     */
    SetmealDto getById(Long id);

    /**
     * 修改套餐信息
     */
    boolean update(SetmealDto setmealDto);

    /**
     * 修改套餐的售卖状态
     * @param status    状态
     * @param ids       ids
     * @return
     */
    boolean upStatus(Integer status, Long[] ids);

    /**
     * 删除套餐
     */
    boolean delete(Long[] ids);

    /**
     * 获取套餐数据
     */
    List<SetmealDto> getSetmealList(Long categoryId, Integer status);

}
