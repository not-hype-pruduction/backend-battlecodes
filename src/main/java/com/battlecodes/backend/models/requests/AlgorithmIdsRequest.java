package com.battlecodes.backend.models.requests;

import lombok.Data;

import java.util.List;

@Data
public class AlgorithmIdsRequest {
    private List<Long> algorithmIds;
}