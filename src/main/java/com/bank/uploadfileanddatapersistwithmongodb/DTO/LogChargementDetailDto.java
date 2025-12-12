package com.bank.uploadfileanddatapersistwithmongodb.DTO;

import com.bank.uploadfileanddatapersistwithmongodb.entity.enums.LineStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogChargementDetailDto {
    private Integer lineNumber;
    private LineStatus status;
    private String detailProblem;
}
