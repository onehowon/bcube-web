package com.ebiz.bcube.domain.activities.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DesigntonDTO {
    private Long id;
    private Integer year;
    private String title;
    private String participants;
    private String imageUrl;
    private String pdfUrl;
}