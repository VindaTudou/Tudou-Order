package io.fangtudou.controller.admin;

import io.fangtudou.result.Result;
import io.fangtudou.service.WorkspaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/admin/workspace")
@Api(tags = "管理端-工作台相关接口")
@Slf4j
public class WorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;

    @GetMapping("/overviewOrders")
    @ApiOperation("今日订单概览")
    public Result<Map<String, Object>> overviewOrders() {
        log.info("查询今日订单概览");
        return Result.success(workspaceService.overviewOrders());
    }

    @GetMapping("/overviewDishes")
    @ApiOperation("菜品总览")
    public Result<Map<String, Object>> overviewDishes() {
        log.info("查询菜品总览");
        return Result.success(workspaceService.overviewDishes());
    }

    @GetMapping("/overviewSetmeals")
    @ApiOperation("套餐总览")
    public Result<Map<String, Object>> overviewSetmeals() {
        log.info("查询套餐总览");
        return Result.success(workspaceService.overviewSetmeals());
    }

    @GetMapping("/businessData")
    @ApiOperation("今日运营数据")
    public Result<Map<String, Object>> businessData() {
        log.info("查询今日运营数据");
        return Result.success(workspaceService.businessData());
    }
}
