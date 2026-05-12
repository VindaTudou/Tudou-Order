package io.fangtudou.service.impl;

import io.fangtudou.constant.StatusConstant;
import io.fangtudou.dto.SetmealDTO;
import io.fangtudou.entity.Setmeal;
import io.fangtudou.entity.SetmealDish;
import io.fangtudou.mapper.SetmealDishMapper;
import io.fangtudou.mapper.SetmealMapper;
import io.fangtudou.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增套餐（含菜品关系）
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        // trim 处理字符串字段
        if (setmeal.getName() != null) {
            setmeal.setName(setmeal.getName().trim());
        }
        if (setmeal.getImage() != null) {
            setmeal.setImage(setmeal.getImage().trim());
        }
        if (setmeal.getDescription() != null) {
            setmeal.setDescription(setmeal.getDescription().trim());
        }

        // 默认状态：停售
        if (setmeal.getStatus() == null) {
            setmeal.setStatus(StatusConstant.DISABLE);
        }

        // 插入套餐（@AutoFill 自动填充时间/操作人）
        setmealMapper.insert(setmeal);

        // 插入套餐菜品关系
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            for (SetmealDish setmealDish : setmealDishes) {
                setmealDish.setSetmealId(setmeal.getId());
            }
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }
}
