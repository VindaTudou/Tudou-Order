package io.fangtudou.service.impl;

import io.fangtudou.constant.StatusConstant;
import io.fangtudou.dto.DishDTO;
import io.fangtudou.entity.Dish;
import io.fangtudou.entity.DishFlavor;
import io.fangtudou.mapper.DishFlavorMapper;
import io.fangtudou.mapper.DishMapper;
import io.fangtudou.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /**
     * 新增菜品（含口味）
     * @param dishDTO
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        //属性拷贝
        BeanUtils.copyProperties(dishDTO, dish);

        //trim 处理字符串字段
        if (dish.getName() != null) {
            dish.setName(dish.getName().trim());
        }
        if (dish.getImage() != null) {
            dish.setImage(dish.getImage().trim());
        }
        if (dish.getDescription() != null) {
            dish.setDescription(dish.getDescription().trim());
        }

        //默认状态：下架
        if (dish.getStatus() == null) {
            dish.setStatus(StatusConstant.DISABLE);
        }

        //插入菜品（@AutoFill 自动填充 createTime/updateTime/createUser/updateUser）
        dishMapper.insert(dish);

        //插入口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            Long dishId = dish.getId();
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dishId);
            }
            dishFlavorMapper.insertBatch(flavors);
        }
    }
}
