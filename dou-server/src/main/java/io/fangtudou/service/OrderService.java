package io.fangtudou.service;

import io.fangtudou.dto.OrdersCancelDTO;
import io.fangtudou.dto.OrdersPageQueryDTO;
import io.fangtudou.dto.OrdersSubmitDTO;
import io.fangtudou.result.PageResult;
import io.fangtudou.vo.OrderSubmitVO;
import io.fangtudou.vo.OrderVO;

import java.util.Map;

public interface OrderService {

    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

    PageResult pageQuery(OrdersPageQueryDTO dto);

    OrderVO getDetails(Long id);

    void complete(Long id);

    void cancel(OrdersCancelDTO dto);

    Map<String, Object> statistics();
}
