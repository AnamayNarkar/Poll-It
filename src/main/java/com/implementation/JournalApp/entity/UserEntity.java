package com.implementation.JournalApp.entity;

import java.util.Date;
import java.util.Vector;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "users")
@Data
@NoArgsConstructor
public class UserEntity {

        @Id
        private ObjectId id;

        @Indexed(unique = true)
        private String username;

        private String password;

        private Vector<String> roles;

        private Date date;

        private Vector<JournalEntryEntity> journalEntries;

        public UserEntity(String username, String password, Vector<String> roles) {
                this.username = username;
                this.password = password;
                this.roles = roles;
                this.date = new Date();
                this.journalEntries = new Vector<>();
        }

        public String getId() {
                return this.id.toHexString();
        }
}
