package com.bank.uploadfileanddatapersistwithmongodb.interfaces;

import com.bank.uploadfileanddatapersistwithmongodb.entity.LogChargement;
import com.bank.uploadfileanddatapersistwithmongodb.entity.enums.LineStatus;
import com.bank.uploadfileanddatapersistwithmongodb.entity.enums.LogStatus;

import java.util.List;

public interface LogChargementService {
    LogChargement startLog(String fileName);

    void addLine(LogChargement log,
                 int lineNumber,
                 LineStatus status,
                 String detailProblem);

    void finalizeLog(LogChargement log,
                     int totalLines,
                     int successLines,
                     int failedLines);

    List<LogChargement> getAllLogs();

    LogChargement getLogById(String id);

    List<LogChargement> searchLogs(String fileName, LogStatus status);
}
