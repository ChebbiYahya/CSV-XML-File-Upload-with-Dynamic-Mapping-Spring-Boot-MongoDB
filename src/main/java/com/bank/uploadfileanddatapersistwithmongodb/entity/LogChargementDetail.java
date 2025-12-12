package com.bank.uploadfileanddatapersistwithmongodb.entity;

import com.bank.uploadfileanddatapersistwithmongodb.entity.enums.LineStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogChargementDetail {
    private Integer lineNumber;
    private LineStatus status;
    private String detailProblem;
}
