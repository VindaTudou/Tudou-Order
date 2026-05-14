package io.fangtudou.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.fangtudou.constant.MessageConstant;
import io.fangtudou.dto.OrdersCancelDTO;
import io.fangtudou.dto.OrdersPageQueryDTO;
import io.fangtudou.dto.OrdersSubmitDTO;
import io.fangtudou.entity.OrderDetail;
import io.fangtudou.entity.Orders;
import io.fangtudou.exception.OrderBusinessException;
import io.fangtudou.mapper.OrderDetailMapper;
import io.fangtudou.mapper.OrdersMapper;
import io.fangtudou.result.PageResult;
import io.fangtudou.service.OrderService;
import io.fangtudou.vo.OrderSubmitVO;
import io.fangtudou.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    @Transactional
    public OrderSubmitVO submit(OrdersSubmitDTO dto) {
        // 生成订单号：时间戳 + UUID前6位
        String number = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        Orders orders = Orders.builder()
                .number(number)
                .status(Orders.ACCEPTED)
                .tableNumber(dto.getTableNumber())
                .amount(dto.getAmount())
                .remark(dto.getRemark())
                .orderTime(LocalDateTime.now())
                .build();

        ordersMapper.insert(orders);
        log.info("订单创建成功, id={}, number={}", orders.getId(), number);

        // 插入订单明细
        List<OrderDetail> orderDetails = dto.getOrderDetails();
        if (orderDetails != null && !orderDetails.isEmpty()) {
            for (OrderDetail detail : orderDetails) {
                detail.setOrderId(orders.getId());
            }
            orderDetailMapper.insertBatch(orderDetails);
        }

        return OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(number)
                .orderAmount(dto.getAmount())
                .orderTime(orders.getOrderTime())
                .build();
    }

    @Override
    public PageResult pageQuery(OrdersPageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        Page<Orders> page = ordersMapper.pageQuery(dto);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public OrderVO getDetails(Long id) {
        Orders orders = ordersMapper.getById(id);
        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(id);

        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetails);
        return orderVO;
    }

    @Override
    public void complete(Long id) {
        Orders orders = Orders.builder()
                .id(id)
                .status(Orders.COMPLETED)
                .completeTime(LocalDateTime.now())
                .build();
        ordersMapper.update(orders);
        log.info("订单出餐完成, id={}", id);
    }

    @Override
    public void cancel(OrdersCancelDTO dto) {
        Orders orders = Orders.builder()
                .id(dto.getId())
                .status(Orders.CANCELLED)
                .cancelReason(dto.getCancelReason())
                .cancelTime(LocalDateTime.now())
                .build();
        ordersMapper.update(orders);
        log.info("订单已取消, id={}, reason={}", dto.getId(), dto.getCancelReason());
    }
}