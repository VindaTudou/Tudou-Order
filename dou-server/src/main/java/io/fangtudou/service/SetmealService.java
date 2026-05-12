package io.fangtudou.service;

import io.fangtudou.dto.SetmealDTO;

public interface SetmealService {

    /**
     * 新增套餐（含菜品关系）
     * @param setmealDTO
     */
    void saveWithDish(SetmealDTO setmealDTO);
}
