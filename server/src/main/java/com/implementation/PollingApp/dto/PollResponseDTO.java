package com.implementation.PollingApp.dto;

import java.util.Date;
import java.util.List;

import com.implementation.PollingApp.entity.PollEntity;

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

        public PollResponseDTO(PollEntity pollEntity, List<OptionResponseDTO> optionResponseDTO) {
                this.id = pollEntity.getId().toHexString();
                this.question = pollEntity.getQuestion();
                this.createdBy = pollEntity.getCreatedBy();
                this.creationDateTime = pollEntity.getCreationDateTime();
                this.expirationDateTime = pollEntity.getExpirationDateTime();
                this.options = optionResponseDTO;
        }

}
