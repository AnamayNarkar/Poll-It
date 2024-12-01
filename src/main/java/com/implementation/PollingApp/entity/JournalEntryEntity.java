package com.implementation.PollingApp.entity;

import java.util.Date;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "journalEntries")
@Data
@NoArgsConstructor
public class JournalEntryEntity {

        @Id
        private ObjectId id;
        private String title;
        private String content;
        private Date date;
        private ObjectId userId;

        public JournalEntryEntity(String title, String content, ObjectId userId) {
                this.title = title;
                this.content = content;
                this.date = new Date();
                this.userId = userId;
        }

        public String getId() {
                return id.toHexString();
        }

        public String getUserId() {
                return userId.toHexString();
        }
}
