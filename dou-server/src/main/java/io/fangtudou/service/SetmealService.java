package io.fangtudou.service;

import io.fangtudou.dto.SetmealDTO;
import io.fangtudou.dto.SetmealPageQueryDTO;
import io.fangtudou.result.PageResult;

public interface SetmealService {

    /**
     * 新增套餐（含菜品关系）
     * @param setmealDTO
     */
    void saveWithDish(SetmealDTO setmealDTO);

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 修改套餐（含菜品关系）
     * @param setmealDTO
     */
    void updateWithDish(SetmealDTO setmealDTO);
}
