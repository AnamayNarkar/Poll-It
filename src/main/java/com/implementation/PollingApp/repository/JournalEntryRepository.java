package com.implementation.PollingApp.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.implementation.PollingApp.entity.JournalEntryEntity;

public interface JournalEntryRepository extends MongoRepository<JournalEntryEntity, ObjectId> {

}