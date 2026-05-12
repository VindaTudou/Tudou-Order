package io.fangtudou.mapper;

import io.fangtudou.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 批量插入套餐菜品关系
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id删除菜品关系
     * @param setmealId
     */
    void deleteBySetmealId(Long setmealId);
}
