package io.fangtudou.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import io.fangtudou.constant.StatusConstant;
import io.fangtudou.dto.SetmealDTO;
import io.fangtudou.dto.SetmealPageQueryDTO;
import io.fangtudou.entity.Category;
import io.fangtudou.entity.Setmeal;
import io.fangtudou.entity.SetmealDish;
import io.fangtudou.mapper.CategoryMapper;
import io.fangtudou.mapper.SetmealDishMapper;
import io.fangtudou.mapper.SetmealMapper;
import io.fangtudou.result.PageResult;
import io.fangtudou.service.SetmealService;
import io.fangtudou.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private CategoryMapper categoryMapper;

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

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<Setmeal> page = setmealMapper.pageQuery(setmealPageQueryDTO);

        // 转换 Setmeal → SetmealVO，填充分类名称
        List<SetmealVO> records = page.getResult().stream().map(setmeal -> {
            SetmealVO setmealVO = new SetmealVO();
            BeanUtils.copyProperties(setmeal, setmealVO);
            if (setmeal.getCategoryId() != null) {
                Category category = categoryMapper.getById(setmeal.getCategoryId());
                if (category != null) {
                    setmealVO.setCategoryName(category.getName());
                }
            }
            return setmealVO;
        }).collect(Collectors.toList());

        return new PageResult(page.getTotal(), records);
    }

    /**
     * 修改套餐（含菜品关系）
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void updateWithDish(SetmealDTO setmealDTO) {
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

        // 更新套餐基本信息
        setmealMapper.update(setmeal);

        // 删除旧菜品关系
        setmealDishMapper.deleteBySetmealId(setmealDTO.getId());

        // 插入新菜品关系
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            for (SetmealDish setmealDish : setmealDishes) {
                setmealDish.setSetmealId(setmealDTO.getId());
            }
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Override
    public SetmealVO getById(Long id) {
        // 查询套餐基本信息
        Setmeal setmeal = setmealMapper.getById(id);

        // 查询套餐菜品关系
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);

        // 组装 SetmealVO
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);

        // 填充分类名称
        if (setmeal.getCategoryId() != null) {
            Category category = categoryMapper.getById(setmeal.getCategoryId());
            if (category != null) {
                setmealVO.setCategoryName(category.getName());
            }
        }

        return setmealVO;
    }
}
