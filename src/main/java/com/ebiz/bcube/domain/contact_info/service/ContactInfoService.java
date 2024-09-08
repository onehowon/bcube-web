package com.ebiz.bcube.domain.contact_info.service;

import com.ebiz.bcube.domain.contact_info.dto.ContactInfoDTO;
import com.ebiz.bcube.domain.contact_info.entity.ContactInfo;
import com.ebiz.bcube.global.exception.RecordNotFoundException;
import com.ebiz.bcube.domain.contact_info.repository.ContactInfoRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ContactInfoService {

    private final ContactInfoRepository repository;

    public ContactInfoService(ContactInfoRepository repository) {
        this.repository = repository;
    }

    public ContactInfoDTO createContactInfo(ContactInfoDTO contactInfoDTO) {
        ContactInfo contactInfo = ContactInfo.builder()
                .email(contactInfoDTO.getEmail())
                .kakaoInfo(contactInfoDTO.getKakaoInfo())
                .instagramInfo(contactInfoDTO.getInstagramInfo())
                .githubInfo(contactInfoDTO.getGithubInfo())
                .build();

        ContactInfo savedContactInfo = repository.save(contactInfo);

        return convertToDto(savedContactInfo);
    }

    public List<ContactInfoDTO> getAllContactInfos() {
        List<ContactInfo> contactInfos = repository.findAll();
        return contactInfos.stream()
                .map(this::convertToDto)
                .toList();
    }

    public ContactInfoDTO getContactInfoById(Long id) {
        return repository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new RecordNotFoundException("연락처 정보를 찾을 수 없습니다. id: " + id));
    }

    public ContactInfoDTO updateContactInfo(Long id, ContactInfoDTO dto) {
        ContactInfo existingContactInfo = repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("연락처 정보를 찾을 수 없습니다. id: " + id));

        ContactInfo updatedContactInfo = ContactInfo.builder()
                .id(existingContactInfo.getId())
                .email(dto.getEmail())
                .kakaoInfo(dto.getKakaoInfo())
                .instagramInfo(dto.getInstagramInfo())
                .githubInfo(dto.getGithubInfo())
                .build();

        ContactInfo savedContactInfo = repository.save(updatedContactInfo);

        return convertToDto(savedContactInfo);
    }

    public void deleteContactInfo(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new RecordNotFoundException("연락처 정보를 찾을 수 없습니다. id: " + id);
        }
    }

    private ContactInfoDTO convertToDto(ContactInfo contactInfo) {
        return ContactInfoDTO.builder()
                .id(contactInfo.getId())
                .email(contactInfo.getEmail())
                .kakaoInfo(contactInfo.getKakaoInfo())
                .instagramInfo(contactInfo.getInstagramInfo())
                .githubInfo(contactInfo.getGithubInfo())
                .build();
    }
}