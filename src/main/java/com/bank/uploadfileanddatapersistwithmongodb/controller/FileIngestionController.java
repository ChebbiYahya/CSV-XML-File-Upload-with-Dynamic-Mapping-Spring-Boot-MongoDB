package com.bank.uploadfileanddatapersistwithmongodb.controller;

import com.bank.uploadfileanddatapersistwithmongodb.interfaces.FileIngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileIngestionController {
    private final FileIngestionService fileIngestionService;

//    public FileIngestionController(FileIngestionService fileIngestionService) {
//        this.fileIngestionService = fileIngestionService;
//    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("type") String type) {
        type = type.toLowerCase();
        int count;

        switch (type) {
            case "csv" -> count = fileIngestionService.ingestCsv(file);
            case "xml" -> count = fileIngestionService.ingestXml(file);
            default -> throw new IllegalArgumentException("Unknown type: " + type + " (expected 'csv' or 'xml')");
        }

        return ResponseEntity.ok("Imported records: " + count);
    }
}
