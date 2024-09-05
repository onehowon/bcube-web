package com.ebiz.bcube.domain.executives.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExecutivesDTO {
    private Long id;
    private String memberName;
    private String role;
    private String studentId;
    private String basicInfo;
    private String imageUrl;
}
