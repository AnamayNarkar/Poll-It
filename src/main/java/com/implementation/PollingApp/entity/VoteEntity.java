package com.implementation.PollingApp.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Document(collection = "votes")
public class VoteEntity {

        @Id
        private ObjectId id;

        private ObjectId pollId;

        private ObjectId optionId;

        private ObjectId userId;

        VoteEntity(String pollId, String optionId, String userId) {
                this.id = new ObjectId();
                this.pollId = new ObjectId(pollId);
                this.optionId = new ObjectId(optionId);
                this.userId = new ObjectId(userId);
        }

        public String getId() {
                return id.toHexString();
        }

        public String getPollId() {
                return pollId.toHexString();
        }

        public String getOptionId() {
                return optionId.toHexString();
        }

        public String getUserId() {
                return userId.toHexString();
        }

}
