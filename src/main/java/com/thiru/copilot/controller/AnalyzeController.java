package com.thiru.copilot.controller;

import com.thiru.copilot.service.AnalyzeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class AnalyzeController {
    private final AnalyzeService analyzeService;

    public AnalyzeController(AnalyzeService analyzeService) {
        this.analyzeService = analyzeService;
    }

    @PostMapping(value = "/analyze", consumes = {"multipart/form-data"})
    public ResponseEntity<?> analyzeResume(
            @RequestParam("resume") MultipartFile file,
            @RequestParam("jd") String jdText) {

        System.out.println("JD RECEIVED: " + jdText);

        return ResponseEntity.ok(
                analyzeService.analyze(file, jdText)
        );
    }
}
