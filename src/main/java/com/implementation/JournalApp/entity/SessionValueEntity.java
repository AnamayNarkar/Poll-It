package com.implementation.JournalApp.entity;

import java.util.Vector;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SessionValueEntity {

        private String id;
        private String username;
        private Vector<String> roles;

}
