package com.ebiz.bcube.domain.activities.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SexyItDTO {
    private Long id;
    private String date;
    private String title;
    private String imageUrl;
    private String url;
}
