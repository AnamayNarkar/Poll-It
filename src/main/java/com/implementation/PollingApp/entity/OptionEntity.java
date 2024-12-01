package com.implementation.PollingApp.entity;

import java.util.List;
import java.util.Vector;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "options")
public class OptionEntity {

        ObjectId id;

        private String option;

        private int voteCount;

        @JsonIgnore
        Vector<ObjectId> votedUserIds;

        public OptionEntity(String option) {
                this.id = new ObjectId();
                this.option = option;
                this.voteCount = 0;
                this.votedUserIds = new Vector<>();
        }

        public String getId() {
                return this.id.toHexString();
        }

        public List<String> getVotedUserIds() {
                return votedUserIds.stream().map(ObjectId::toHexString).toList();
        }
}