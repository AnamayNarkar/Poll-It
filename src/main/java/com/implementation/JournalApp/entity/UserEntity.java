package com.implementation.JournalApp.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Vector;

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

        @DBRef
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