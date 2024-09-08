package com.ebiz.bcube.domain.contact_info.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContactInfoDTO {
    private Long id;
    private String email;
    private String kakaoInfo;
    private String instagramInfo;
    private String githubInfo;
}
