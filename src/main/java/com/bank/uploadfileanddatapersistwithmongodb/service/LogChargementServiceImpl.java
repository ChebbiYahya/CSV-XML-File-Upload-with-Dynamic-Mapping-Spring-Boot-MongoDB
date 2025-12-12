package com.bank.uploadfileanddatapersistwithmongodb.service;

import com.bank.uploadfileanddatapersistwithmongodb.entity.LogChargement;
import com.bank.uploadfileanddatapersistwithmongodb.entity.LogChargementDetail;
import com.bank.uploadfileanddatapersistwithmongodb.entity.enums.LineStatus;
import com.bank.uploadfileanddatapersistwithmongodb.entity.enums.LogStatus;
import com.bank.uploadfileanddatapersistwithmongodb.exception.LogChargementNotFoundException;
import com.bank.uploadfileanddatapersistwithmongodb.interfaces.LogChargementService;
import com.bank.uploadfileanddatapersistwithmongodb.repository.LogChargementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogChargementServiceImpl implements LogChargementService {
    private final LogChargementRepository logChargementRepository;

    @Override
    public LogChargement startLog(String fileName) {
        LogChargement log = LogChargement.builder()
                .fileName(fileName)
                .status(LogStatus.IN_PROGRESS)
                .createdAt(LocalDateTime.now())
                .details(new ArrayList<>())
                .build();
        return logChargementRepository.save(log);
    }

    @Override
    public void addLine(LogChargement log, int lineNumber, LineStatus status, String detailProblem) {
        LogChargementDetail detail = LogChargementDetail.builder()
                .lineNumber(lineNumber)
                .status(status)
                .detailProblem(detailProblem)
                .build();

        log.addDetail(detail);
        logChargementRepository.save(log);
    }

    @Override
    public void finalizeLog(LogChargement log, int totalLines, int successLines, int failedLines) {
        log.setTotalLines(totalLines);
        log.setSuccessLines(successLines);
        log.setFailedLines(failedLines);

        if (successLines > 0 && failedLines == 0) {
            log.setStatus(LogStatus.SUCCESS);
        } else if (successLines == 0 && failedLines > 0) {
            log.setStatus(LogStatus.FAILED);
        } else if (successLines > 0 && failedLines > 0) {
            log.setStatus(LogStatus.PARTIALLY_TRAITED);
        } else {
            log.setStatus(LogStatus.FAILED);
        }

        logChargementRepository.save(log);
    }

    @Override
    public List<LogChargement> getAllLogs() {
        return logChargementRepository.findAll();
    }

    @Override
    public LogChargement getLogById(String id) {
        return logChargementRepository.findById(id)
                .orElseThrow(() -> new LogChargementNotFoundException("LogChargement not found with id: " + id));
    }

    @Override
    public List<LogChargement> searchLogs(String fileName, LogStatus status) {
        // même logique que MySQL: filtre en mémoire (simple et fiable)
        return logChargementRepository.findAll().stream()
                .filter(log -> {
                    if (fileName == null || fileName.isBlank()) return true;
                    String fn = log.getFileName();
                    return fn != null && fn.toLowerCase().contains(fileName.toLowerCase());
                })
                .filter(log -> status == null || status.equals(log.getStatus()))
                .toList();
    }
}
