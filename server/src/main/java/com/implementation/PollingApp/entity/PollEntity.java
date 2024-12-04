package com.implementation.PollingApp.entity;

import java.util.Date;
// import java.util.List;
import java.util.Vector;

import org.bson.types.ObjectId;
// import org.springframework.data.mongodb.core.mapping.DBRef;
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

        private Vector<ObjectId> options;

        private Vector<ObjectId> tags;

        public PollEntity(String question, String createdBy, Vector<ObjectId> options, Date expirationDateTime, Vector<ObjectId> tags) {

                if (tags.size() == 0 || tags.size() > 3) {
                        throw new IllegalArgumentException("Tags should be between 1 and 3");
                }

                this.id = new ObjectId();
                this.question = question;
                this.createdBy = createdBy;
                this.options = options;
                this.creationDateTime = new Date();
                this.expirationDateTime = expirationDateTime;
        }
}