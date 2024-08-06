package com.example.iotplatform.controller;

import com.example.iotplatform.exception.CustomException;
import com.example.iotplatform.model.NaturalGasProduction;
import com.example.iotplatform.service.NaturalGasProductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/production")
public class NaturalGasProductionController {

    private final NaturalGasProductionService service;

    @Autowired
    public NaturalGasProductionController(NaturalGasProductionService service) {
        this.service = service;
    }

    @PostMapping("/import")
    public ResponseEntity<String> importCsvData(@RequestParam String filePath) {
        try {
            service.importCsvData(filePath);
            return ResponseEntity.ok("Data imported successfully");
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<NaturalGasProduction>> getProductionByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new CustomException("Start date must be before or equal to end date");
        }
        return ResponseEntity.ok(service.getProductionByDateRange(startDate, endDate));
    }

    @GetMapping("/date")
    public ResponseEntity<NaturalGasProduction> getProductionByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        NaturalGasProduction production = service.getProductionByDate(date);
        if (production != null) {
            return ResponseEntity.ok(production);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/top-production")
    public ResponseEntity<List<NaturalGasProduction>> getTopProductionDays(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(service.getTopProductionDays(startDate, endDate, limit));
    }

    @GetMapping("/total-by-state")
    public ResponseEntity<Map<String, Double>> getTotalProductionByState() {
        return ResponseEntity.ok(service.getTotalProductionByState());
    }
}