package com.ebiz.bcube.domain.contact_info.api;

import com.ebiz.bcube.domain.contact_info.dto.ContactInfoDTO;
import com.ebiz.bcube.domain.contact_info.service.ContactInfoService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contact-info")
public class ContactInfoController {

    private final ContactInfoService contactInfoService;

    public ContactInfoController(ContactInfoService contactInfoService) {
        this.contactInfoService = contactInfoService;
    }

    @PostMapping
    public ResponseEntity<ContactInfoDTO> createContactInfo(@RequestBody ContactInfoDTO contactInfoDTO) {
        ContactInfoDTO createdContactInfo = contactInfoService.createContactInfo(contactInfoDTO);
        return new ResponseEntity<>(createdContactInfo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ContactInfoDTO>> getAllContactInfos() {
        List<ContactInfoDTO> contactInfos = contactInfoService.getAllContactInfos();
        return new ResponseEntity<>(contactInfos, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactInfoDTO> updateContactInfo(
            @PathVariable Long id,
            @RequestBody ContactInfoDTO contactInfoDTO) {
        ContactInfoDTO updatedContactInfo = contactInfoService.updateContactInfo(id, contactInfoDTO);
        return ResponseEntity.ok(updatedContactInfo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactInfo(@PathVariable Long id) {
        contactInfoService.deleteContactInfo(id);
        return ResponseEntity.noContent().build();
    }
}