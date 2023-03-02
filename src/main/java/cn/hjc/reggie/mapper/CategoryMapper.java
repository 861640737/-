package cn.hjc.reggie.mapper;

import cn.hjc.reggie.domain.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 查询分类数据分页
     * @param current
     * @param pageSize
     * @return
     */
    @Select({"<script>" +
            " select * from category " +
            " order by sort asc, id asc " +
            " limit #{current}, #{pageSize} " +
            "</script>"})
    List<Category> selectAllByPage(Integer current, Integer pageSize);

    /**
     * 根据类型查询分类
     * @param type  1.菜品    2.套餐
     * @return
     */
    @Select({"<script>" +
            " select * from category " +
            " where type = #{type} " +
            "</script>"})
    List<Category> selectByType(Integer type);

    /**
     * 根据id查询分类信息
     * @param id
     * @return
     */
    @Select(" select * from category where id = #{id} ")
    Category selectById(Long id);

    /**
     * 查询所有分类
     * @return
     */
    @Select("select * from category")
    List<Category> selectAll();
}
