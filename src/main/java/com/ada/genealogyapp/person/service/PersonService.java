package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.person.model.Person;

import java.util.UUID;

public interface PersonService {

    Person findPersonByIdOrThrowNodeNotFoundException(UUID personId);
}
