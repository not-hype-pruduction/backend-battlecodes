package com.battlecodes.backend.models.requests;

import lombok.Data;

@Data
public class UploadAlgorithmRequest {
    private String language;
    private byte[] file;
}