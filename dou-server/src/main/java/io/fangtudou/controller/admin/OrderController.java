package io.fangtudou.controller.admin;

import io.fangtudou.dto.OrdersCancelDTO;
import io.fangtudou.dto.OrdersPageQueryDTO;
import io.fangtudou.result.PageResult;
import io.fangtudou.result.Result;
import io.fangtudou.service.OrderService;
import io.fangtudou.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/order")
@Api(tags = "管理端-订单相关接口")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/conditionSearch")
    @ApiOperation("分页查询订单")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO dto) {
        log.info("订单分页查询: status={}", dto.getStatus());
        PageResult pageResult = orderService.pageQuery(dto);
        return Result.success(pageResult);
    }

    @GetMapping("/details/{id}")
    @ApiOperation("查询订单详情")
    public Result<OrderVO> details(@PathVariable Long id) {
        OrderVO orderVO = orderService.getDetails(id);
        return Result.success(orderVO);
    }

    @PutMapping("/complete/{id}")
    @ApiOperation("出餐完成")
    public Result<String> complete(@PathVariable Long id) {
        log.info("订单出餐完成: id={}", id);
        orderService.complete(id);
        return Result.success();
    }

    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public Result<String> cancel(@RequestBody OrdersCancelDTO dto) {
        log.info("取消订单: id={}, reason={}", dto.getId(), dto.getCancelReason());
        orderService.cancel(dto);
        return Result.success();
    }
}
