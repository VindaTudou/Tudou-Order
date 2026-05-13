package io.fangtudou.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders implements Serializable {

    /**
     * 订单状态 1已接单 2已出餐 3已取消
     */
    public static final Integer ACCEPTED = 1;
    public static final Integer COMPLETED = 2;
    public static final Integer CANCELLED = 3;

    private static final long serialVersionUID = 1L;

    private Long id;

    //订单号（时间戳+自增号）
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

    //出餐时间
    private LocalDateTime completeTime;

    //订单取消原因
    private String cancelReason;

    //订单取消时间
    private LocalDateTime cancelTime;
}
