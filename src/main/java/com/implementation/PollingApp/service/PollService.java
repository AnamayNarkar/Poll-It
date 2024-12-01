package com.implementation.PollingApp.service;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

import com.implementation.PollingApp.dto.PollEntryDTO;
import com.implementation.PollingApp.entity.OptionEntity;
import com.implementation.PollingApp.entity.PollEntity;
import com.implementation.PollingApp.repository.OptionRepository;
import com.implementation.PollingApp.repository.PollRepository;

@Service
public class PollService {

        @Autowired
        private PollRepository pollRepository;

        @Autowired
        private OptionRepository optionRepository;

        // @Transactional
        public PollEntity createPoll(PollEntryDTO pollEntryDTO, String username) {
                List<OptionEntity> savedOptionEntities = pollEntryDTO.getOptions().stream().map(OptionEntity::new).collect(Collectors.toList());
                savedOptionEntities = optionRepository.saveAll(savedOptionEntities);
                PollEntity pollEntity = new PollEntity(pollEntryDTO.getQuestion(), username, new Vector<>(savedOptionEntities));
                return pollRepository.save(pollEntity);
        }
}
