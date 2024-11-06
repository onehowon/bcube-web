package com.ebiz.bcube.domain.activities.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(Include.NON_NULL)
public class EtcDTO {
    private Long id;
    private Integer year;
    private String title;
    private String participants;
    private String imageUrl;
    private String url;
    private String pdfUrl;
}
