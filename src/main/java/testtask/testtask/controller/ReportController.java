package testtask.testtask.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import testtask.testtask.dto.DailyReportDto;
import testtask.testtask.service.ReportService;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/daily/{userId}/date/{date}")
    public ResponseEntity<DailyReportDto> getDailyReport(
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(reportService.generateDailyReport(userId, date));
    }

    @GetMapping("/compliance/{userId}/date/{date}")
    public ResponseEntity<Boolean> checkCalorieNormCompliance(
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(reportService.checkCalorieNormCompliance(userId, date));
    }

    @GetMapping("/history/{userId}/from/{startDate}/to/{endDate}")
    public ResponseEntity<Map<LocalDate, DailyReportDto>> getMealHistoryReport(
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportService.generateMealHistoryReport(userId, startDate, endDate));
    }
} 