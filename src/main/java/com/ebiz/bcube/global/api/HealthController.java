package com.ebiz.bcube.global.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/api/healthy")
    public ResponseEntity<String> checkHealth() {
        return new ResponseEntity<>("서버가 정상 동작 중입니다.", HttpStatus.OK);
    }
}