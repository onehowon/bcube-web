package com.ebiz.bcube.domain.ob_interview.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ObInterviewDTO {
    private Long id;
    private String name;
    private String studentId;
    private String message;
    private String imageUrl;
    private String email;
}
