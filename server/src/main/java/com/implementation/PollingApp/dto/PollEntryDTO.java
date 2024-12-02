package com.implementation.PollingApp.dto;

import java.util.Date;
import java.util.Vector;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PollEntryDTO {

        String question;
        Vector<String> options;
        Date expirationDateTime;

}
