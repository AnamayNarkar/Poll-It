package com.implementation.PollingApp.dto;

import java.util.Date;
import java.util.Vector;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollEntryDTO {

        String question;
        Vector<String> options;
        Date expirationDateTime;
        Vector<TagWithoutPollsDTO> tags;
}