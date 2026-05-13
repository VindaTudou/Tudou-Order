package io.fangtudou.mapper;

import io.fangtudou.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 批量插入口味数据
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 根据菜品id删除口味数据
     * @param dishId
     */
    void deleteByDishId(Long dishId);

    /**
     * 根据菜品id查询口味数据
     * @param dishId
     * @return
     */
    List<DishFlavor> getByDishId(Long dishId);

    void deleteByDishIds(@Param("ids") List<Long> ids);
}
