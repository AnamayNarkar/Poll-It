package com.implementation.PollingApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionResponseDTO {
    String id;

    private String option;

    private int voteCount;
}
