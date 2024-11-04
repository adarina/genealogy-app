package com.ada.genealogyapp.person.service;
;
import com.ada.genealogyapp.person.dto.PersonResponse;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.repostitory.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PersonViewService {

    private final PersonSearchService personSearchService;

    private final PersonRepository personRepository;

    public PersonViewService(PersonSearchService personSearchService, PersonRepository personRepository) {
        this.personSearchService = personSearchService;
        this.personRepository = personRepository;
    }

    public Page<PersonResponse> getPersons(UUID treeId, Pageable pageable) {
        Page<Person> personsPage = personSearchService.getPersonsByTreeId(treeId, pageable);

        List<PersonResponse> personResponses = personsPage.getContent().stream()
                .map(person -> new PersonResponse(
                        person.getId(),
                        person.getFirstname(),
                        person.getLastname(),
                        person.getBirthdate(),
                        person.getGender()))
                .collect(Collectors.toList());

        return new PageImpl<>(personResponses, pageable, personsPage.getTotalElements());
    }


}
