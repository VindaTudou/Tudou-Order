package io.fangtudou.controller.user;

import io.fangtudou.constant.MessageConstant;
import io.fangtudou.dto.OrdersSubmitDTO;
import io.fangtudou.exception.OrderBusinessException;
import io.fangtudou.result.Result;
import io.fangtudou.service.OrderService;
import io.fangtudou.vo.OrderSubmitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/order")
@Api(tags = "用户端-订单相关接口")
@Slf4j
public class UserOrderController {

    private static final String SHOP_STATUS_KEY = "shop:status";

    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/submit")
    @ApiOperation("提交订单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        // 检查店铺营业状态
        Object statusObj = redisTemplate.opsForValue().get(SHOP_STATUS_KEY);
        int status = 1;
        if (statusObj instanceof Number) {
            status = ((Number) statusObj).intValue();
        }
        if (status != 1) {
            log.info("店铺休息中，拒绝下单");
            throw new OrderBusinessException(MessageConstant.SHOP_CLOSED);
        }

        log.info("用户下单: tableNumber={}, amount={}", ordersSubmitDTO.getTableNumber(), ordersSubmitDTO.getAmount());
        OrderSubmitVO vo = orderService.submit(ordersSubmitDTO);
        return Result.success(vo);
    }
}