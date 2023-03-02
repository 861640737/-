package cn.hjc.reggie.dto;

import cn.hjc.reggie.domain.OrderDetail;
import cn.hjc.reggie.domain.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
