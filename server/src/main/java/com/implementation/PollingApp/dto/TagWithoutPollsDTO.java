package com.implementation.PollingApp.dto;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagWithoutPollsDTO {
        private String id;
        private String name;
}
