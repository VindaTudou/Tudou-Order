package io.fangtudou.service.impl;

import io.fangtudou.mapper.DishMapper;
import io.fangtudou.mapper.OrdersMapper;
import io.fangtudou.mapper.SetmealMapper;
import io.fangtudou.service.WorkspaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public Map<String, Object> overviewOrders() {
        // Status: 1=已接单, 2=已出餐, 3=已取消
        Integer accepted = ordersMapper.countByStatusToday(1);
        Integer completed = ordersMapper.countByStatusToday(2);
        Integer cancelled = ordersMapper.countByStatusToday(3);
        Integer all = ordersMapper.countToday();

        Map<String, Object> map = new HashMap<>();
        map.put("waitingOrders", accepted != null ? accepted : 0);
        map.put("deliveredOrders", completed != null ? completed : 0);
        map.put("completedOrders", completed != null ? completed : 0);
        map.put("cancelledOrders", cancelled != null ? cancelled : 0);
        map.put("allOrders", all != null ? all : 0);
        return map;
    }

    @Override
    public Map<String, Object> overviewDishes() {
        Integer sold = dishMapper.countByStatus(1);
        Integer discontinued = dishMapper.countByStatus(0);

        Map<String, Object> map = new HashMap<>();
        map.put("sold", sold != null ? sold : 0);
        map.put("discontinued", discontinued != null ? discontinued : 0);
        return map;
    }

    @Override
    public Map<String, Object> overviewSetmeals() {
        Integer sold = setmealMapper.countByStatus(1);
        Integer discontinued = setmealMapper.countByStatus(0);

        Map<String, Object> map = new HashMap<>();
        map.put("sold", sold != null ? sold : 0);
        map.put("discontinued", discontinued != null ? discontinued : 0);
        return map;
    }

    @Override
    public Map<String, Object> businessData() {
        Double turnover = ordersMapper.sumTurnoverToday();
        Integer validOrders = ordersMapper.countValidOrdersToday();
        Integer allOrders = ordersMapper.countToday();

        double orderCompletionRate = 0.0;
        if (allOrders != null && allOrders > 0 && validOrders != null) {
            orderCompletionRate = (double) validOrders / allOrders;
        }

        double unitPrice = 0.0;
        if (validOrders != null && validOrders > 0 && turnover != null) {
            unitPrice = turnover / validOrders;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("turnover", turnover != null ? BigDecimal.valueOf(turnover).setScale(2, RoundingMode.HALF_UP) : 0);
        map.put("validOrderCount", validOrders != null ? validOrders : 0);
        map.put("orderCompletionRate", BigDecimal.valueOf(orderCompletionRate).setScale(2, RoundingMode.HALF_UP));
        map.put("unitPrice", BigDecimal.valueOf(unitPrice).setScale(2, RoundingMode.HALF_UP));
        map.put("newUsers", 0); // No user system
        return map;
    }
}
