package io.fangtudou.dto;

import io.fangtudou.entity.OrderDetail;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrdersDTO implements Serializable {

    private Long id;

    //订单号
    private String number;

    //订单状态 1已接单 2已出餐 3已取消
    private Integer status;

    //桌号
    private Integer tableNumber;

    //总金额
    private BigDecimal amount;

    //备注
    private String remark;

    //下单时间
    private LocalDateTime orderTime;

    private List<OrderDetail> orderDetails;

}
