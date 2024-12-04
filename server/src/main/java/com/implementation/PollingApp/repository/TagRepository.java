package com.implementation.PollingApp.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.implementation.PollingApp.dto.TagWithouPollsDTO;
import com.implementation.PollingApp.entity.TagEntity;

public interface TagRepository extends MongoRepository<TagEntity, ObjectId> {
    TagEntity findByName(String name);

    List<TagEntity> findAllByNameIn(List<String> names);

    @Query(value = "{ 'name': { $in: ?0 } }", fields = "{ 'pollIds': 0 }")
    List<TagWithouPollsDTO> findAllByNameWithoutPollIdsIn(List<String> names);

    @Query(value = "{ 'id': { $in: ?0 } }", fields = "{ 'pollIds': 0 }")
    List<TagWithouPollsDTO> findAllByIdWithoutPollIdsIn(List<ObjectId> ids);

}
