package io.fangtudou.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.Page;
import io.fangtudou.constant.MessageConstant;
import io.fangtudou.constant.StatusConstant;
import io.fangtudou.dto.SetmealDTO;
import io.fangtudou.dto.SetmealPageQueryDTO;
import io.fangtudou.entity.Category;
import io.fangtudou.entity.Setmeal;
import io.fangtudou.entity.SetmealDish;
import io.fangtudou.exception.DeletionNotAllowedException;
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

    @Override
    @Transactional
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        trimStringFields(setmeal);

        if (setmeal.getStatus() == null) {
            setmeal.setStatus(StatusConstant.DISABLE);
        }

        setmealMapper.insert(setmeal);

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            for (SetmealDish setmealDish : setmealDishes) {
                setmealDish.setSetmealId(setmeal.getId());
            }
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<Setmeal> page = setmealMapper.pageQuery(setmealPageQueryDTO);

        List<SetmealVO> records = page.getResult().stream().map(setmeal -> {
            SetmealVO setmealVO = new SetmealVO();
            BeanUtils.copyProperties(setmeal, setmealVO);
            fillCategoryName(setmealVO, setmeal.getCategoryId());
            return setmealVO;
        }).collect(Collectors.toList());

        return new PageResult(page.getTotal(), records);
    }

    @Override
    @Transactional
    public void updateWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        trimStringFields(setmeal);

        setmealMapper.update(setmeal);
        setmealDishMapper.deleteBySetmealId(setmealDTO.getId());

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            for (SetmealDish setmealDish : setmealDishes) {
                setmealDish.setSetmealId(setmealDTO.getId());
            }
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    @Override
    public SetmealVO getById(Long id) {
        Setmeal setmeal = setmealMapper.getById(id);
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);

        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        fillCategoryName(setmealVO, setmeal.getCategoryId());

        return setmealVO;
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        if (setmealMapper.anyEnabled(ids) > 0) {
            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        }

        setmealDishMapper.deleteBySetmealIds(ids);
        setmealMapper.deleteByIds(ids);
    }

    private void trimStringFields(Setmeal setmeal) {
        if (setmeal.getName() != null) {
            setmeal.setName(setmeal.getName().trim());
        }
        if (setmeal.getImage() != null) {
            setmeal.setImage(setmeal.getImage().trim());
        }
        if (setmeal.getDescription() != null) {
            setmeal.setDescription(setmeal.getDescription().trim());
        }
    }

    private void fillCategoryName(SetmealVO vo, Long categoryId) {
        if (categoryId != null) {
            Category category = categoryMapper.getById(categoryId);
            if (category != null) {
                vo.setCategoryName(category.getName());
            }
        }
    }
}
