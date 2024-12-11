package com.implementation.PollingApp.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

@Document(collection = "users")
@Data
@NoArgsConstructor
public class UserEntity {

        @Id
        private ObjectId id;

        @Indexed(unique = true)
        private String username;

        @Indexed(unique = true)
        private String email;

        // @JsonIgnore
        private String password;

        private Vector<String> roles;

        private Date joinDate;

        private Vector<ObjectId> createdPolls;

        private Map<ObjectId, ObjectId> votedPolls;

        private Vector<ObjectId> followedTags;

        private String profilePictureURL;

        private String bio;

        public UserEntity(String username, String email, String password, Vector<String> roles) {
                this.username = username;
                this.password = password;
                this.email = email;
                this.roles = roles;
                this.joinDate = new Date();
                this.createdPolls = new Vector<>();
                this.votedPolls = new HashMap<>();
                this.followedTags = new Vector<>();
                this.profilePictureURL = "https://www.pngkey.com/png/full/114-1149878_setting-user-avatar-in-specific-size-without-breaking.png";
                this.bio = "";
        }
}