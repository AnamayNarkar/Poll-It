package com.implementation.PollingApp.entity;

import java.util.Map;
import java.util.Vector;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedValueEntity {
        private Vector<String> followedTags;
        private Map<String, String> feedState;
}