package io.fangtudou.controller.admin;

import io.fangtudou.constant.MessageConstant;
import io.fangtudou.exception.ImageDownLoadErrorException;
import io.fangtudou.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

    @Value("${tudou.upload.path}")
    private String uploadPath;

    /**
     * 上传图片
     * @param file
     * @return Result
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传:{}", file);

        //得到文件的原始名称
        String originalFilename = file.getOriginalFilename();
        //截取文件的后缀
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        //拼接UUID与文件后缀
        String objectName = UUID.randomUUID().toString() + extension;
        //创建目录
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            //保存文件至指定路径
            file.transferTo(new File(uploadPath + objectName));
        } catch (IOException e) {
            log.error("文件上传失败:{}", e);
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
        //返回上传成功的文件名
        return Result.success("/images/" + objectName);
    }

    /**
     * 删除图片
     * @param name
     * @return
     */
    @DeleteMapping("/upload")
    @ApiOperation("文件删除")
    public Result<String> delete(@RequestParam String name) {
        log.info("文件删除:{}", name);

        File file = new File(uploadPath + name);
        if (file.exists()) {
            file.delete();
        }
        return Result.success();
    }

    // TODO 图片上传时可修改成确认创建新菜品后再上传至本地，而不是每次上传图片都直接上传本地。
    // TODO 删除菜品时再从本地删除图片数据
}
