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
@Table(name = "etc")
public class Etc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "year")
    private Integer year;

    @Column(name = "title")
    private String title;

    @Column(name = "participants")
    private String participants;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "url")
    private String url;
}