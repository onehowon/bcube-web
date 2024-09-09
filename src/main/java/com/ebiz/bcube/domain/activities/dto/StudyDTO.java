package com.ebiz.bcube.domain.activities.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudyDTO {
    private Long id;
    private Integer year;
    private String title;
    private String imageUrl;
    private String participants;
    private String url;
}
