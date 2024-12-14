package com.implementation.PollingApp.entity;

import java.util.Map;
import java.util.Set;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedValueEntity {
    private List<String> followedTags;
    private Map<String, String> feedState;
    private Set<String> sentPostIds;
}