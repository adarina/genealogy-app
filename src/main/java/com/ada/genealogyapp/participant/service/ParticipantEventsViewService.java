package com.ada.genealogyapp.participant.service;

import com.ada.genealogyapp.participant.dto.ParticipantEventResponse;
import com.ada.genealogyapp.participant.repository.ParticipantRepository;
import com.ada.genealogyapp.person.dto.params.GetParticipantEventsParams;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParticipantEventsViewService {

    private final ParticipantRepository participantRepository;

    private final TreeService treeService;


    public Page<ParticipantEventResponse> getParticipantEvents(GetParticipantEventsParams params) {
        Page<ParticipantEventResponse> page = participantRepository.findParticipantEvents(params.getUserId(), params.getTreeId(), params.getParticipantId(), params.getPageable());
        treeService.ensureUserAndTreeExist(params, page);
        return page;
    }
}