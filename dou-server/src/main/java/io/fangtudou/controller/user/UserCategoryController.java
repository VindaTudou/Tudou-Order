package io.fangtudou.controller.user;

import io.fangtudou.entity.Category;
import io.fangtudou.result.Result;
import io.fangtudou.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/category")
@Api(tags = "用户端-分类相关接口")
@Slf4j
public class UserCategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    @ApiOperation("查询已启用的分类")
    public Result<List<Category>> list() {
        List<Category> categories = categoryService.list(null);
        return Result.success(categories);
    }
}
