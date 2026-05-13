package io.fangtudou.dto;

import io.fangtudou.entity.OrderDetail;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrdersSubmitDTO implements Serializable {

    //桌号
    private Integer tableNumber;

    //备注
    private String remark;

    //总金额
    private BigDecimal amount;

    //订单明细
    private List<OrderDetail> orderDetails;
}
