package com.ada.genealogyapp.location.service;


import com.ada.genealogyapp.location.dto.GeographyResponse;
import com.ada.genealogyapp.location.dto.LocationFilterRequest;
import com.ada.genealogyapp.location.dto.params.GetLocationsParams;
import com.ada.genealogyapp.location.repository.LocationRepository;
import com.ada.genealogyapp.tree.service.TreeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GeographyViewService {

    private final LocationRepository locationRepository;

    private final ObjectMapper objectMapper;

    private final TreeService treeService;

    public List<GeographyResponse> getGeographies(GetLocationsParams params) throws JsonProcessingException {
        LocationFilterRequest filterRequest = objectMapper.readValue(params.getFilter(), LocationFilterRequest.class);
        List<GeographyResponse> geographyResponses = locationRepository.find(params.getUserId(), params.getTreeId(), filterRequest.getName(), filterRequest.getType());
        treeService.ensureUserAndTreeExist(params, geographyResponses);
        return geographyResponses;
    }
}
