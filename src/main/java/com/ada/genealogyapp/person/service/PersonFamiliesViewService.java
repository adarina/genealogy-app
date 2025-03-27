package com.ada.genealogyapp.person.service;


import com.ada.genealogyapp.person.dto.PersonFamilyResponse;
import com.ada.genealogyapp.person.dto.params.GetPersonFamiliesParams;
import com.ada.genealogyapp.person.repository.PersonRepository;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonFamiliesViewService {

    private final PersonRepository personRepository;

    private final TreeService treeService;

    public Page<PersonFamilyResponse> getPersonalFamilies(GetPersonFamiliesParams params) {
        Page<PersonFamilyResponse> page = personRepository.findFamilies(params.getUserId(), params.getTreeId(), params.getPersonId(), params.getPageable());
        treeService.ensureUserAndTreeExist(params, page);
        return page;
    }
}