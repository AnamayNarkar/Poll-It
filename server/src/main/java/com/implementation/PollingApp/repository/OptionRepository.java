package com.implementation.PollingApp.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.Optional;
import com.implementation.PollingApp.dto.OptionResponseDTO;
import com.implementation.PollingApp.entity.OptionEntity;

public interface OptionRepository extends MongoRepository<OptionEntity, ObjectId> {

    @Query(value = "{ 'id' : ?0 }", fields = "{ id: 1, option: 1, voteCount: 1 }")
    Optional<OptionResponseDTO> findByIdWithoutVotes(ObjectId id);
}
