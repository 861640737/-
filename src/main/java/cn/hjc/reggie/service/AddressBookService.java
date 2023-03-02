package cn.hjc.reggie.service;

import cn.hjc.reggie.domain.AddressBook;

import java.util.List;

/**
 * 地址
 */
public interface AddressBookService {

    /**
     * 根据用户id获取该所有的地址
     * @param id
     * @return
     */
    List<AddressBook> getAddressBookByUserId(Long id);

    /**
     * 保存地址
     * @param addressBook
     * @return
     */
    boolean saveAddress(AddressBook addressBook);

    /**
     * 将地址修改成默认
     * @param addressBook
     * @return
     */
    boolean updateDefaultAddress(AddressBook addressBook);

    /**
     * 根据用户id获取默认地址
     */
    AddressBook getDefaultAddress(Long userId);
}
