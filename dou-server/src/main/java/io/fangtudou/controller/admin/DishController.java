package io.fangtudou.controller.admin;

import io.fangtudou.dto.DishDTO;
import io.fangtudou.dto.DishPageQueryDTO;
import io.fangtudou.entity.Dish;
import io.fangtudou.result.PageResult;
import io.fangtudou.result.Result;
import io.fangtudou.service.DishService;
import io.fangtudou.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     * @param dishDTO
     * @return Result
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品：{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 修改菜品
     * @param dishDTO
     * @return Result
     */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品：{}", dishDTO);
        dishService.updateWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 批量删除菜品
     * @param ids
     * @return Result
     */
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("批量删除菜品：{}", ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return Result
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据id查询菜品：{}", id);
        DishVO dishVO = dishService.getById(id);
        return Result.success(dishVO);
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return Result
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> listByCategoryId(@RequestParam Long categoryId) {
        log.info("根据分类id查询菜品：{}", categoryId);
        List<Dish> list = dishService.listByCategoryId(categoryId);
        return Result.success(list);
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return Result
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询：{}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 菜品起售、停售
     * @param status
     * @param id
     * @return Result
     */
    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售、停售")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        log.info("菜品起售停售：status={}, id={}", status, id);
        dishService.startOrStop(status, id);
        return Result.success();
    }
}
