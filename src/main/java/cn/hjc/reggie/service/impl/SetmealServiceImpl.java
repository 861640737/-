package cn.hjc.reggie.service.impl;

import cn.hjc.reggie.common.BaseContext;
import cn.hjc.reggie.domain.*;
import cn.hjc.reggie.dto.SetmealDto;
import cn.hjc.reggie.mapper.CategoryMapper;
import cn.hjc.reggie.mapper.SetmealMapper;
import cn.hjc.reggie.service.SetmealService;
import cn.hjc.reggie.util.KeyGeneration;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    SetmealMapper setmealMapper;

    @Autowired
    CategoryMapper categoryMapper;

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @Override
    @Transactional
    public boolean save(SetmealDto setmealDto) {
        setmealDto.setId(KeyGeneration.getId());
        LocalDateTime time = LocalDateTime.now();
        setmealDto.setCreateTime(time);
        setmealDto.setUpdateTime(time);
        setmealDto.setCreateUser(BaseContext.getCurrentId());
        setmealDto.setUpdateUser(BaseContext.getCurrentId());

        Integer insertSetmeal = setmealMapper.insertSetmeal(setmealDto);
        Long setSetmealId = setmealDto.getId();

        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes().stream().map((item) -> {
            item.setId(KeyGeneration.getId());
            item.setSetmealId(setSetmealId);
            item.setCreateTime(time);
            item.setUpdateTime(time);
            item.setCreateUser(BaseContext.getCurrentId());
            item.setUpdateUser(BaseContext.getCurrentId());
            return item;
        }).collect(Collectors.toList());

        Integer insertSetmealDish = setmealMapper.insertSetmealDish(setmealDishList);

        return insertSetmeal > 0 && insertSetmealDish > 0;
    }

    @Override
    public Page<SetmealDto> getSetmealDtoPage(Integer current, Integer pageSize, String name) {
        Page<SetmealDto> page = new Page<>();

        if (name == null) {
            name = "%%";
        } else {
            name = "%" + name + "%";
        }

        //根据条件获取总记录数
        Integer total = setmealMapper.getTotal(name);
        if (total == null || total < 1){
            page.setTotal(total);
            page.setCurrent(current);
            page.setPageSize(pageSize);
            return page;
        }

        //若页码小于1,默认改为1
        if (current == null || current < 1) current = 1;
        //每页数小于1,默认改为3
        if (pageSize == null || pageSize < 1) pageSize = 10;
        //页码数大于最大页码数,则改为最大页码数
        Integer currentMax = total % pageSize == 0? total / pageSize: total / pageSize + 1;
        if (current > currentMax) current = currentMax;

        page.setTotal(total);
        page.setCurrent(current);
        page.setPageSize(pageSize);

        List<Setmeal> setmealList = setmealMapper.getAllPage((current - 1) * pageSize, pageSize, name);

        List<SetmealDto> setmealDtoList = setmealList.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            setmealDto.setCategoryName(categoryMapper.selectById(setmealDto.getCategoryId()).getName());
            return setmealDto;
        }).collect(Collectors.toList());

        page.setRecords(setmealDtoList);
        return page;
    }

    @Override
    public SetmealDto getById(Long id) {
        SetmealDto setmealDto = setmealMapper.getById(id);
        setmealDto.setSetmealDishes(setmealMapper.getDishBySetmealId(id));
        return setmealDto;
    }

    @Override
    @Transactional
    public boolean update(SetmealDto setmealDto) {
        Integer upSetmeal = setmealMapper.upSetmeal(setmealDto);

        List<SetmealDish> newSetmealDishList = setmealDto.getSetmealDishes();
        List<SetmealDish> oldSetmealDishList = setmealMapper.getDishBySetmealId(setmealDto.getId());

        setmealMapper.delDish(setmealDto.getId());
        if (newSetmealDishList == null || newSetmealDishList.size() < 1) {
            return true;
        }

        if (oldSetmealDishList == null || oldSetmealDishList.size() < 1) {
            setmealMapper.insertSetmealDish(newSetmealDishList);
            return true;
        }

        List<SetmealDish> lists = new ArrayList<>();

        Long employee = BaseContext.getCurrentId();
        LocalDateTime time = LocalDateTime.now();

        for (int i = 0; i < newSetmealDishList.size(); i++) {
            SetmealDish setmealDish = newSetmealDishList.get(i);
            for (int j = 0; j < oldSetmealDishList.size(); j ++) {
                if (Objects.equals(setmealDish.getDishId(), oldSetmealDishList.get(j).getDishId())) {
                    setmealDish.setId(oldSetmealDishList.get(j).getId());
                    setmealDish.setSetmealId(setmealDto.getId());
                    setmealDish.setCreateTime(oldSetmealDishList.get(j).getCreateTime());
                    setmealDish.setCreateUser(oldSetmealDishList.get(i).getCreateUser());
                    break;
                }
            }
            setmealDish.setUpdateTime(time);
            setmealDish.setUpdateUser(employee);
            if (setmealDish.getCreateUser() == null) {
                setmealDish.setId(KeyGeneration.getId());
                setmealDish.setCreateUser(employee);
                setmealDish.setCreateTime(time);
            }
            lists.add(setmealDish);
        }

        setmealMapper.insertSetmealDish(lists);
        return true;
    }

    @Override
    @Transactional
    public boolean upStatus(Integer status, Long[] ids) {
        for (int i = 0; i < ids.length; i++) {
            setmealMapper.upStatus(status, ids[i]);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean delete(Long[] ids) {
        for (int i = 0; i < ids.length; i++) {
            setmealMapper.del(ids[i]);
            setmealMapper.delDish(ids[i]);
        }
        return true;
    }

    @Override
    public List<SetmealDto> getSetmealList(Long categoryId, Integer status) {
        List<Setmeal> list = setmealMapper.getByCategoryIdAndStatus(categoryId, status);
        List<SetmealDto> setmealDtoList = list.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            setmealDto.setSetmealDishes(setmealMapper.getDishBySetmealId(setmealDto.getId()));
            return setmealDto;
        }).collect(Collectors.toList());

        return setmealDtoList;
    }
}
