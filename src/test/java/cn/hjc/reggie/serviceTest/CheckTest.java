package cn.hjc.reggie.serviceTest;

import cn.hjc.reggie.util.CheckCodeUtil;
import org.junit.jupiter.api.Test;

public class CheckTest {

    @Test
    public void check() {
        System.out.println(CheckCodeUtil.getCheckCode());
    }

}
