package io.fangtudou.service;

import io.fangtudou.dto.SetmealDTO;
import io.fangtudou.dto.SetmealPageQueryDTO;
import io.fangtudou.result.PageResult;
import io.fangtudou.vo.SetmealVO;

import java.util.List;

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

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    SetmealVO getById(Long id);

    /**
     * 套餐起售、停售
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 批量删除套餐
     * @param ids
     */
    void deleteBatch(List<Long> ids);
}
