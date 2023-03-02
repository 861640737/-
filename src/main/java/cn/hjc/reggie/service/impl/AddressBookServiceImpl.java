package cn.hjc.reggie.service.impl;

import cn.hjc.reggie.domain.AddressBook;
import cn.hjc.reggie.mapper.AddressBookMapper;
import cn.hjc.reggie.service.AddressBookService;
import cn.hjc.reggie.util.KeyGeneration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Override
    public List<AddressBook> getAddressBookByUserId(Long id) {
        List<AddressBook> addressBooks = addressBookMapper.getByUserId(id);
        return addressBooks;
    }

    @Override
    public boolean saveAddress(AddressBook addressBook) {
        //设置用户的创建修改信息
        LocalDateTime time = LocalDateTime.now();
        if (addressBook.getCreateTime() == null) {
            addressBook.setId(KeyGeneration.getId());
            addressBook.setCreateTime(time);
            addressBook.setCreateUser(addressBook.getUserId());
        }
        addressBook.setUpdateTime(time);
        addressBook.setUpdateUser(addressBook.getUserId());

        int i = addressBookMapper.insert(addressBook);
        return i > 0;
    }

    @Override
    @Transactional
    public boolean updateDefaultAddress(AddressBook addressBook) {
        // 1.将此用户的地址全部取消默认
        addressBookMapper.cancelDefaultAddress(addressBook.getUserId());
        // 2.将指定地址设置为默认
        Integer setDefaultAddress = addressBookMapper.setDefaultAddress(addressBook.getId());

        return setDefaultAddress > 0;
    }

    @Override
    public AddressBook getDefaultAddress(Long userId) {
        return addressBookMapper.getDefault(userId);
    }


}
