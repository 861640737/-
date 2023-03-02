package cn.hjc.reggie.mapper;

import cn.hjc.reggie.domain.AddressBook;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 地址sql
 */
@Mapper
public interface AddressBookMapper {


    /**
     * 根据用户id获取所有地址
     * @param id
     * @return
     */
    @Select("select * from address_book where user_id = #{id}")
    List<AddressBook> getByUserId(Long id);


    /**
     * 保存地址
     * @param addressBook
     * @return
     */
    @Insert("insert into address_book (id, user_id, consignee, sex, phone, detail, label," +
            " create_time, update_time, create_user, update_user) values (#{id}, #{userId}, #{consignee}, " +
            " #{sex}, #{phone}, #{detail}, #{label}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    Integer insert(AddressBook addressBook);

    /**
     * 根据用户id将地址全部取消默认
     * @param userId
     * @return
     */
    @Update("update address_book set is_default = 0 where user_id = #{userId}")
    Integer cancelDefaultAddress(Long userId);

    /**
     * 根据id将地址设置为默认
     * @param id
     * @return
     */
    @Update("update address_book set is_default = 1 where id = #{id}")
    Integer setDefaultAddress(Long id);

    @Select("select * from address_book where user_id = #{userId} and is_default = 1")
    AddressBook getDefault(Long userId);

}
