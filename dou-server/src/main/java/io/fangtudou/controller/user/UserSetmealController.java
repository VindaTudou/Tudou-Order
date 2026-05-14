package io.fangtudou.controller.user;

import io.fangtudou.constant.StatusConstant;
import io.fangtudou.dto.SetmealPageQueryDTO;
import io.fangtudou.result.PageResult;
import io.fangtudou.result.Result;
import io.fangtudou.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/setmeal")
@Api(tags = "用户端-套餐相关接口")
@Slf4j
public class UserSetmealController {

    @Autowired
    private SetmealService setmealService;

    @GetMapping("/page")
    @ApiOperation("分页查询已上架套餐")
    public Result<PageResult> page(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int pageSize,
                                   @RequestParam(required = false) Integer categoryId) {
        SetmealPageQueryDTO dto = new SetmealPageQueryDTO();
        dto.setPage(page);
        dto.setPageSize(pageSize);
        dto.setCategoryId(categoryId);
        dto.setStatus(StatusConstant.ENABLE);
        PageResult pageResult = setmealService.pageQuery(dto);
        return Result.success(pageResult);
    }
}
