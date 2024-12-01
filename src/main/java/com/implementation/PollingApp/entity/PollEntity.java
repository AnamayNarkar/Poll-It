package com.implementation.PollingApp.entity;

import java.util.Date;
// import java.util.List;
import java.util.Vector;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "polls")
public class PollEntity {

        private ObjectId id;

        private String question;

        private String createdBy;

        private Date creationDateTime;

        private Date expirationDateTime;

        @DBRef
        private Vector<OptionEntity> options;

        public PollEntity(String question, String createdBy, Vector<OptionEntity> options, Date expirationDateTime) {
                this.id = new ObjectId();
                this.question = question;
                this.createdBy = createdBy;
                this.options = options;
                this.creationDateTime = new Date();
                this.expirationDateTime = expirationDateTime;
        }

        public String getId() {
                return this.id.toHexString();
        }

}