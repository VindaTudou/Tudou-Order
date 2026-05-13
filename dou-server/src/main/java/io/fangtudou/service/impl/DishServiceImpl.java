package io.fangtudou.service.impl;

import io.fangtudou.constant.MessageConstant;
import io.fangtudou.constant.StatusConstant;
import io.fangtudou.dto.DishDTO;
import io.fangtudou.dto.DishPageQueryDTO;
import io.fangtudou.entity.Category;
import io.fangtudou.entity.Dish;
import io.fangtudou.entity.DishFlavor;
import io.fangtudou.exception.DeletionNotAllowedException;
import io.fangtudou.mapper.CategoryMapper;
import io.fangtudou.mapper.DishFlavorMapper;
import io.fangtudou.mapper.DishMapper;
import io.fangtudou.result.PageResult;
import io.fangtudou.service.DishService;
import io.fangtudou.vo.DishVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private CategoryMapper categoryMapper;

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

    /**
     * 修改菜品（含口味）
     * @param dishDTO
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // trim 处理字符串字段
        if (dish.getName() != null) {
            dish.setName(dish.getName().trim());
        }
        if (dish.getImage() != null) {
            dish.setImage(dish.getImage().trim());
        }
        if (dish.getDescription() != null) {
            dish.setDescription(dish.getDescription().trim());
        }

        // 更新菜品基本信息
        dishMapper.update(dish);

        // 删除旧口味数据
        dishFlavorMapper.deleteByDishId(dishDTO.getId());

        // 插入新口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dishDTO.getId());
            }
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 批量删除菜品
     * @param ids
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 批量查询，检查是否有起售中的菜品
        List<Dish> dishes = dishMapper.getByIds(ids);
        for (Dish dish : dishes) {
            if (StatusConstant.ENABLE.equals(dish.getStatus())) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        // 批量检查是否有菜品关联了套餐
        Integer count = dishMapper.countSetmealByDishIds(ids);
        if (count != null && count > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        // 批量删除口味数据
        dishFlavorMapper.deleteByDishIds(ids);

        // 批量删除菜品
        dishMapper.deleteByIds(ids);
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @Override
    public DishVO getById(Long id) {
        // 查询菜品基本信息
        Dish dish = dishMapper.getById(id);

        // 查询口味数据
        List<DishFlavor> flavors = dishFlavorMapper.getByDishId(id);

        // 组装 DishVO
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);

        // Dish 实体没有 categoryName，需单独查询分类表
        if (dish.getCategoryId() != null) {
            Category category = categoryMapper.getById(dish.getCategoryId());
            if (category != null) {
                dishVO.setCategoryName(category.getName());
            }
        }

        return dishVO;
    }

    /**
     * 根据分类id查询菜品列表
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> listByCategoryId(Long categoryId) {
        return dishMapper.listByCategoryId(categoryId);
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<Dish> page = dishMapper.pageQuery(dishPageQueryDTO);
        List<Dish> result = page.getResult();

        // 批量查询分类名称（一次 IN 查询解决 N+1）
        List<Long> categoryIds = result.stream()
                .map(Dish::getCategoryId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, String> categoryNameMap = categoryIds.isEmpty()
                ? Collections.emptyMap()
                : categoryMapper.listByIds(categoryIds).stream()
                    .collect(Collectors.toMap(Category::getId, Category::getName));

        // 转换 Dish → DishVO
        List<DishVO> records = result.stream().map(dish -> {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(dish, dishVO);
            dishVO.setCategoryName(categoryNameMap.get(dish.getCategoryId()));
            return dishVO;
        }).collect(Collectors.toList());

        return new PageResult(page.getTotal(), records);
    }

    /**
     * 菜品起售、停售
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.update(dish);
    }
}
