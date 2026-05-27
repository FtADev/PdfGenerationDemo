package com.example.demo.controller;

import com.example.demo.model.Item;
import com.example.demo.service.DataService;
import com.example.demo.service.ExcelGeneratorService;
import com.example.demo.service.PdfGeneratorService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/reports")
public class ReportController {

    private final PdfGeneratorService pdfGeneratorService;
    private final ExcelGeneratorService excelGeneratorService;
    private final DataService dataService;

    public ReportController(PdfGeneratorService pdfGeneratorService,
                            ExcelGeneratorService excelGeneratorService,
                            DataService dataService) {
        this.pdfGeneratorService = pdfGeneratorService;
        this.excelGeneratorService = excelGeneratorService;
        this.dataService = dataService;
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> generatePdf(
            @RequestParam(value = "title", defaultValue = "SALES REPORT") String title,
            @RequestParam(value = "generatedBy", defaultValue = "System Administrator") String generatedBy,
            @RequestParam(value = "department", defaultValue = "Sales Department") String department) {

        // Generate 100 sample records
        List<Item> items = dataService.generateSampleData(100);

        // Prepare variables for template
        Map<String, Object> variables = new HashMap<>();
        variables.put("title", title);
        variables.put("generatedBy", generatedBy);
        variables.put("department", department);
        variables.put("date", dataService.getCurrentDate());
        variables.put("time", dataService.getCurrentTime());
        variables.put("items", items);

        // Generate PDF
        byte[] pdfBytes = pdfGeneratorService.generatePdf("report-template", variables);

        // Return PDF as response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "sales-report.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @GetMapping("/excel")
    public ResponseEntity<byte[]> generateExcel(
            @RequestParam(value = "title", defaultValue = "SALES REPORT") String title,
            @RequestParam(value = "generatedBy", defaultValue = "System Administrator") String generatedBy,
            @RequestParam(value = "department", defaultValue = "Sales Department") String department) {

        // Generate 100 sample records
        List<Item> items = dataService.generateSampleData(100);

        // Generate Excel
        byte[] excelBytes = excelGeneratorService.generateExcel(items, title, generatedBy, department);

        // Return Excel as response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "sales-report.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelBytes);
    }

    @GetMapping("/html/preview")
    public String previewHtml(
            @RequestParam(value = "title", defaultValue = "SALES REPORT") String title,
            @RequestParam(value = "generatedBy", defaultValue = "System Administrator") String generatedBy,
            @RequestParam(value = "department", defaultValue = "Sales Department") String department,
            Map<String, Object> model) {

        List<Item> items = dataService.generateSampleData(100);

        model.put("title", title);
        model.put("generatedBy", generatedBy);
        model.put("department", department);
        model.put("date", dataService.getCurrentDate());
        model.put("time", dataService.getCurrentTime());
        model.put("items", items);

        return "report-template";
    }
}