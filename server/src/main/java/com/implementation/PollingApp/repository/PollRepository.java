package com.implementation.PollingApp.repository;

import java.time.Instant;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.implementation.PollingApp.entity.PollEntity;

public interface PollRepository extends MongoRepository<PollEntity, ObjectId> {

        PollEntity findByQuestion(String question);

        List<PollEntity> findByCreatedBy(String createdBy);

        List<PollEntity> findByCreatedBy(String createdBy, Pageable pageable);

        List<PollEntity> findByTags(ObjectId tagId, Pageable pageable);

        @Query(value = "{ 'tags': ?0 }", sort = "{ '_id': -1 }")
        List<PollEntity> findLatestPollForThisTag(ObjectId tagId, Pageable pageable);

        @Query(value = "{ 'tags': ?0, '_id': { $lt: ?1 } }", sort = "{ '_id': -1 }")
        List<PollEntity> findImmediatePollBefore(ObjectId tagId, ObjectId pollId, Pageable pageable);

        @Query(value = "{ 'creationDateTime': { $gte: ?0 } }", sort = "{ 'totalVoteCount': -1 }")
        List<PollEntity> findPopularPollsByDateRange(Instant startDate, Pageable pageable);

}