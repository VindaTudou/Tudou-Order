package io.fangtudou.service;

import io.fangtudou.dto.DishDTO;
import io.fangtudou.dto.DishPageQueryDTO;
import io.fangtudou.entity.Dish;
import io.fangtudou.result.PageResult;
import io.fangtudou.vo.DishVO;

import java.util.List;

public interface DishService {

    /**
     * 新增菜品（含口味）
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);

    /**
     * 修改菜品（含口味）
     * @param dishDTO
     */
    void updateWithFlavor(DishDTO dishDTO);

    /**
     * 批量删除菜品
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    DishVO getById(Long id);

    /**
     * 根据分类id查询菜品列表
     * @param categoryId
     * @return
     */
    List<Dish> listByCategoryId(Long categoryId);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 菜品起售、停售
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);
}
