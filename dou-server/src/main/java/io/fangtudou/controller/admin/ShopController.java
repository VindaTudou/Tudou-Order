package io.fangtudou.controller.admin;

import io.fangtudou.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {

    private static final String REDIS_KEY = "shop:status";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/status")
    @ApiOperation("获取营业状态")
    public Result<Integer> getStatus() {
        Object status = redisTemplate.opsForValue().get(REDIS_KEY);
        if (status == null) {
            redisTemplate.opsForValue().set(REDIS_KEY, 1);
            return Result.success(1);
        }
        return Result.success((Integer) status);
    }

    @PutMapping("/{status}")
    @ApiOperation("切换营业状态")
    public Result<String> updateStatus(@PathVariable Integer status) {
        log.info("切换营业状态: {}", status == 1 ? "营业中" : "打烊中");
        redisTemplate.opsForValue().set(REDIS_KEY, status);
        return Result.success();
    }
}