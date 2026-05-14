package io.fangtudou.service;

import java.util.Map;

public interface ReportService {

    Map<String, Object> turnoverStatistics(String begin, String end);

    Map<String, Object> userStatistics(String begin, String end);

    Map<String, Object> ordersStatistics(String begin, String end);

    Map<String, Object> top10(String begin, String end);

    String export();
}
