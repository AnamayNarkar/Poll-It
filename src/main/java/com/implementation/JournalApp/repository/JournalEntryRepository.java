package com.implementation.JournalApp.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.implementation.JournalApp.entity.JournalEntryEntity;

public interface JournalEntryRepository extends MongoRepository<JournalEntryEntity, ObjectId> {

}