package com.ebiz.bcube.domain.main_activity.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder

public class MainActivityDTO {
    private Long id;
    private String imagePath;
    private String title;
    private String pdfPath;
    private String description;
}
