package com.ebiz.bcube.domain.activity_photo.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ActivityPhotoDTO {
    private Long id;
    private String description;
    private String imageUrl;
    private LocalDate date;
}
