package io.fangtudou.service;

import io.fangtudou.dto.DishDTO;

public interface DishService {

    /**
     * 新增菜品（含口味）
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);
}
