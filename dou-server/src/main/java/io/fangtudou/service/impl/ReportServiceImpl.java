package io.fangtudou.service.impl;

import io.fangtudou.mapper.OrdersMapper;
import io.fangtudou.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Override
    public Map<String, Object> turnoverStatistics(String begin, String end) {
        // Return date-total pairs for chart
        Double total = ordersMapper.sumTurnoverByDate(begin, end);

        Map<String, Object> map = new HashMap<>();
        List<String> dateList = new ArrayList<>();
        List<Double> turnoverList = new ArrayList<>();
        dateList.add(begin);
        turnoverList.add(total != null ? total : 0.0);

        map.put("dateList", dateList);
        map.put("turnoverList", turnoverList);
        // Simplified: return single aggregate value
        map.put("total", total != null ? total : 0.0);
        return map;
    }

    @Override
    public Map<String, Object> userStatistics(String begin, String end) {
        // No user system in this project
        Map<String, Object> map = new HashMap<>();
        map.put("newUserList", new ArrayList<>());
        map.put("totalUserList", new ArrayList<>());
        map.put("dateList", new ArrayList<>());
        return map;
    }

    @Override
    public Map<String, Object> ordersStatistics(String begin, String end) {
        Integer total = ordersMapper.countByStatusAndDate(null, begin, end);
        Integer completed = ordersMapper.countByStatusAndDate(2, begin, end);
        Integer cancelled = ordersMapper.countByStatusAndDate(3, begin, end);

        Map<String, Object> map = new HashMap<>();
        map.put("allOrderCount", total != null ? total : 0);
        map.put("validOrderCount", completed != null ? completed : 0);
        map.put("cancelledOrderCount", cancelled != null ? cancelled : 0);
        return map;
    }

    @Override
    public Map<String, Object> top10(String begin, String end) {
        // Simplified: return empty data
        Map<String, Object> map = new HashMap<>();
        map.put("nameList", new ArrayList<>());
        map.put("numberList", new ArrayList<>());
        return map;
    }

    @Override
    public String export() {
        Integer total = ordersMapper.countToday();
        Double turnover = ordersMapper.sumTurnoverToday();

        StringBuilder sb = new StringBuilder();
        sb.append("\uFEFF"); // BOM for Excel UTF-8
        sb.append("土豆点餐 - 运营数据报表\n");
        sb.append("今日订单总数,").append(total != null ? total : 0).append("\n");
        sb.append("今日营业额,").append(turnover != null ? turnover : 0).append("\n");
        return sb.toString();
    }
}
