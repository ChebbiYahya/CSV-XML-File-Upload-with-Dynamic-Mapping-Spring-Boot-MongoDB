package com.bank.uploadfileanddatapersistwithmongodb.controller;

import com.bank.uploadfileanddatapersistwithmongodb.DTO.LogChargementSummaryDto;
import com.bank.uploadfileanddatapersistwithmongodb.DTO.LogChargementWithDetailsDto;
import com.bank.uploadfileanddatapersistwithmongodb.entity.LogChargement;
import com.bank.uploadfileanddatapersistwithmongodb.entity.enums.LogStatus;
import com.bank.uploadfileanddatapersistwithmongodb.interfaces.LogChargementService;
import com.bank.uploadfileanddatapersistwithmongodb.mapper.LogChargementMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogChargementController {
    private final LogChargementService logChargementService;
    private final LogChargementMapper logChargementMapper;

    @GetMapping
    public List<LogChargementSummaryDto> getAllLogs(
            @RequestParam(name = "fileName", required = false) String fileName,
            @RequestParam(name = "status", required = false) LogStatus status
    ) {
        return logChargementService.searchLogs(fileName, status).stream()
                .sorted(Comparator.comparing(LogChargement::getCreatedAt).reversed())
                .map(logChargementMapper::toSummaryDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public LogChargementWithDetailsDto getLogById(@PathVariable String id) {
        LogChargement log = logChargementService.getLogById(id);
        return logChargementMapper.toWithDetailsDto(log);
    }
}
