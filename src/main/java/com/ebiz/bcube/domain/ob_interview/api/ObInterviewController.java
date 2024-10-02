package com.ebiz.bcube.domain.ob_interview.api;

import com.ebiz.bcube.domain.ob_interview.dto.ObInterviewDTO;
import com.ebiz.bcube.domain.ob_interview.service.ObInterviewService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ob-interviews")
public class ObInterviewController {

    private final ObInterviewService obInterviewService;
    private final String bucketUrl;

    public ObInterviewController(ObInterviewService obInterviewService, @Value("${oracle.cloud.bucket-url}") String bucketUrl) {
        this.obInterviewService = obInterviewService;
        this.bucketUrl = bucketUrl;
    }

    @PostMapping
    public ResponseEntity<ObInterviewDTO> createObInterview(
            @RequestParam("name") String name,
            @RequestParam("student_id") String studentId,
            @RequestParam("message") String message,
            @RequestParam("image") MultipartFile multipartFile,
            @RequestParam("email") String email) throws IOException {

        ObInterviewDTO createdObInterview = obInterviewService.createObInterview(name, studentId, message, multipartFile, email);
        return new ResponseEntity<>(createdObInterview, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ObInterviewDTO>> getAllObInterviews() {
        List<ObInterviewDTO> obInterviews = obInterviewService.getAllObInterviews();
        return new ResponseEntity<>(obInterviews, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObInterviewDTO> getObInterviewById(@PathVariable Long id) {
        ObInterviewDTO obInterview = obInterviewService.getObInterviewById(id);
        return ResponseEntity.ok(obInterview);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ObInterviewDTO> updateObInterview(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("student_id") String studentId,
            @RequestParam("message") String message,
            @RequestParam("image") MultipartFile multipartFile,
            @RequestParam("email") String email) throws IOException {
        ObInterviewDTO updatedObInterview = obInterviewService.updateObInterview(id, name, studentId, message, multipartFile, email);
        return ResponseEntity.ok(updatedObInterview);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteObInterview(@PathVariable Long id) {
        obInterviewService.deleteObInterview(id);
        return ResponseEntity.noContent().build();
    }
}