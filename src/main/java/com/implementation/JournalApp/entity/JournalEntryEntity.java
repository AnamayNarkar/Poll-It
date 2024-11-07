package com.implementation.JournalApp.entity;

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

        public JournalEntryEntity(String title, String content) {
                this.title = title;
                this.content = content;
                this.date = new Date();
        }

        public String getId() {
                return id.toHexString();
        }
}
