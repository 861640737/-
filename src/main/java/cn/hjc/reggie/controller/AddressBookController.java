package cn.hjc.reggie.controller;

import cn.hjc.reggie.common.R;
import cn.hjc.reggie.domain.AddressBook;
import cn.hjc.reggie.service.AddressBookService;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 地址
 */
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 获取所有地址
     * @param request
     * @return
     */
    @GetMapping("/list")
    public R<List<AddressBook>> getAllAddressBook(HttpServletRequest request) {
        Long id = (Long) request.getSession().getAttribute("userId");
        List<AddressBook> addressBookList = addressBookService.getAddressBookByUserId(id);
        return R.success(addressBookList);
    }


    /**
     * 添加地址
     * @param addressBook
     * @param request
     * @return
     */
    @PostMapping
    public R<String> saveAddress(@RequestBody AddressBook addressBook, HttpServletRequest request) {
        addressBook.setUserId((Long) request.getSession().getAttribute("userId"));
        boolean flag = addressBookService.saveAddress(addressBook);
        if (flag == true) {
            return R.success("保存成功~~");
        }
        return R.error("保存失败~~");
    }

    /**
     * 将地址设置为默认地址
     * @return
     */
    @PutMapping("/default")
    public R<String> setUpDefaultAddress(@RequestBody AddressBook addressBook, HttpServletRequest request) {
        addressBook.setUserId((Long) request.getSession().getAttribute("userId"));
        boolean flag = addressBookService.updateDefaultAddress(addressBook);
        if (flag) {
            return R.success("设置成功~~");
        }
        return R.error("服务器繁忙,请稍后再试~~");
    }

    /**
     * 获取用户的默认地址
     */
    @GetMapping("/default")
    public R<AddressBook> getDefault(HttpServletRequest request) {
        AddressBook addressBook = addressBookService.getDefaultAddress((Long) request.getSession().getAttribute("userId"));
        return R.success(addressBook);
    }

}
