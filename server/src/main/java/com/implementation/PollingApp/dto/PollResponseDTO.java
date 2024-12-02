package com.implementation.PollingApp.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollResponseDTO {

        private String id;

        private String question;

        private String createdBy;

        private Date creationDateTime;

        private Date expirationDateTime;

        private List<OptionResponseDTO> options;

}
