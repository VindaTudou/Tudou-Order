package io.fangtudou.service;

import java.util.Map;

public interface WorkspaceService {

    Map<String, Object> overviewOrders();

    Map<String, Object> overviewDishes();

    Map<String, Object> overviewSetmeals();

    Map<String, Object> businessData();
}
