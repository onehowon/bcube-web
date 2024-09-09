package com.ebiz.bcube.domain.activities.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sexy_it")
public class SexyIt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private String date;

    @Column(name = "title")
    private String title;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "url")
    private String url;
}
