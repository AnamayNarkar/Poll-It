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

    private List<TagWithoutPollsDTO> tags;

    private List<OptionResponseDTO> options;

    private Boolean hasUserVotedForThisPoll;

    private String optionIdVotedFor;

    private Integer totalVoteCount;

    private Integer totalCommentCount;

    public PollResponseDTO(PollEntity pollEntity, List<OptionResponseDTO> optionResponseDTO, List<TagWithoutPollsDTO> tags) {
        this.id = pollEntity.getId().toHexString();
        this.question = pollEntity.getQuestion();
        this.createdBy = pollEntity.getCreatedBy();
        this.creationDateTime = pollEntity.getCreationDateTime();
        this.expirationDateTime = pollEntity.getExpirationDateTime();
        this.tags = tags;
        this.options = optionResponseDTO;
        this.hasUserVotedForThisPoll = false;
        this.optionIdVotedFor = null;
        this.totalVoteCount = pollEntity.getTotalVoteCount();
        this.totalCommentCount = pollEntity.getTotalCommentCount();
    }

    public PollResponseDTO(PollEntity pollEntity, List<OptionResponseDTO> optionResponseDTO, List<TagWithoutPollsDTO> tags, Boolean hasUserVotedForThisPoll, String optionIdVotedFor) {
        this.id = pollEntity.getId().toHexString();
        this.question = pollEntity.getQuestion();
        this.createdBy = pollEntity.getCreatedBy();
        this.creationDateTime = pollEntity.getCreationDateTime();
        this.expirationDateTime = pollEntity.getExpirationDateTime();
        this.tags = tags;
        this.options = optionResponseDTO;
        this.hasUserVotedForThisPoll = hasUserVotedForThisPoll;
        this.optionIdVotedFor = optionIdVotedFor;
        this.totalVoteCount = pollEntity.getTotalVoteCount();
        this.totalCommentCount = pollEntity.getTotalCommentCount();
    }

}
