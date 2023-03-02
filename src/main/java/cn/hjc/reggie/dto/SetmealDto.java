package cn.hjc.reggie.dto;

import cn.hjc.reggie.domain.Setmeal;
import cn.hjc.reggie.domain.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
