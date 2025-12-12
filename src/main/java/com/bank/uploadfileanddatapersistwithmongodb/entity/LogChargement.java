package com.bank.uploadfileanddatapersistwithmongodb.entity;
import com.bank.uploadfileanddatapersistwithmongodb.entity.enums.LogStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Document(collection = "log_chargement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogChargement {
    @Id
    private String id; // Mongo ObjectId (String)

    private String fileName;
    private LogStatus status;
    private LocalDateTime createdAt;

    private Integer totalLines;
    private Integer successLines;
    private Integer failedLines;

    @Builder.Default
    private List<LogChargementDetail> details = new ArrayList<>();

    public void addDetail(LogChargementDetail detail) {
        if (details == null) details = new ArrayList<>();
        details.add(detail);
    }

}
