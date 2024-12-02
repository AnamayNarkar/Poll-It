package com.implementation.PollingApp.entity;

import java.util.Vector;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

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

        Vector<ObjectId> votes;

        public OptionEntity(String option) {
                this.id = new ObjectId();
                this.option = option;
                this.voteCount = 0;
                this.votes = new Vector<>();
        }
}