package io.fangtudou.controller.admin;

import io.fangtudou.result.Result;
import io.fangtudou.service.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/admin/report")
@Api(tags = "管理端-数据统计相关接口")
@Slf4j
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额统计")
    public Result<Map<String, Object>> turnoverStatistics(@RequestParam String begin, @RequestParam String end) {
        log.info("营业额统计: {} ~ {}", begin, end);
        return Result.success(reportService.turnoverStatistics(begin, end));
    }

    @GetMapping("/userStatistics")
    @ApiOperation("用户统计")
    public Result<Map<String, Object>> userStatistics(@RequestParam String begin, @RequestParam String end) {
        log.info("用户统计: {} ~ {}", begin, end);
        return Result.success(reportService.userStatistics(begin, end));
    }

    @GetMapping("/ordersStatistics")
    @ApiOperation("订单统计")
    public Result<Map<String, Object>> ordersStatistics(@RequestParam String begin, @RequestParam String end) {
        log.info("订单统计: {} ~ {}", begin, end);
        return Result.success(reportService.ordersStatistics(begin, end));
    }

    @GetMapping("/top10")
    @ApiOperation("菜品销量TOP10")
    public Result<Map<String, Object>> top10(@RequestParam String begin, @RequestParam String end) {
        log.info("菜品销量TOP10: {} ~ {}", begin, end);
        return Result.success(reportService.top10(begin, end));
    }

    @GetMapping("/export")
    @ApiOperation("导出运营数据")
    public void export(HttpServletResponse response) {
        log.info("导出运营数据报表");
        try {
            String data = reportService.export();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=report.csv");
            OutputStream os = response.getOutputStream();
            os.write(data.getBytes(StandardCharsets.UTF_8));
            os.flush();
            os.close();
        } catch (Exception e) {
            log.error("导出报表失败", e);
        }
    }
}
