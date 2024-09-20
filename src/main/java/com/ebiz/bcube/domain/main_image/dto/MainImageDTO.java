package com.ebiz.bcube.domain.main_image.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MainImageDTO {
    private Long id;
    private String imageUrl;
}