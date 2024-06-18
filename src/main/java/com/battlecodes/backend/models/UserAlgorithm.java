package com.battlecodes.backend.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "userAlgorithm")
public class UserAlgorithm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "language")
    private String language;

    @Column(length = 10000)
    @Lob
    private byte[] bytes;
}
