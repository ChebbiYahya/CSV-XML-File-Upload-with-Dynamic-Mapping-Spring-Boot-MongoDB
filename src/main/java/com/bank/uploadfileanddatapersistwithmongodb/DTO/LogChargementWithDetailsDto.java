package com.bank.uploadfileanddatapersistwithmongodb.DTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogChargementWithDetailsDto {
    private String id;
    private String fileName;
    private String status;
    private String createdAt;

    private Integer totalLines;
    private Integer successLines;
    private Integer failedLines;

    private List<LogChargementDetailDto> details;
}
