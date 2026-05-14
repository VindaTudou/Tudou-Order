package io.fangtudou.controller.user;

import io.fangtudou.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/shop")
@Api(tags = "用户端-店铺相关接口")
@Slf4j
public class UserShopController {

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
        if (status instanceof Number) {
            return Result.success(((Number) status).intValue());
        }
        return Result.success(1);
    }
}
