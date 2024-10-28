package com.ada.genealogyapp.family.integration;

import com.ada.genealogyapp.config.IntegrationTestConfig;
import com.ada.genealogyapp.family.dto.UUIDRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.relationship.FamilyRelationship;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.relationship.PersonRelationship;
import com.ada.genealogyapp.person.repostitory.PersonRepository;
import com.ada.genealogyapp.person.type.GenderType;
import com.ada.genealogyapp.person.type.PersonRelationshipType;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class FamilyPersonsViewIntegrationTest extends IntegrationTestConfig {


    @Autowired
    TreeRepository treeRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    FamilyRepository familyRepository;


    @BeforeEach
    void setUp() {
        treeRepository.deleteAll();
        personRepository.deleteAll();
        familyRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
//        treeRepository.deleteAll();
//        familyRepository.deleteAll();
//        personRepository.deleteAll();
    }

    @Test
    void shouldAddExistingFatherAndExistingChildToFamilySuccessfully() throws Exception {

        Tree tree = new Tree();
        treeRepository.save(tree);

        Person child = new Person("Amalia Smith", "Amalia", "Smith", LocalDate.of(2000, 12, 18), GenderType.FEMALE, tree);
        personRepository.save(child);

        Person mother = new Person("Elizabeth Black", "Elizabeth", "Black", LocalDate.of(1975, 7, 8), GenderType.FEMALE, tree);
        personRepository.save(mother);

        Person father = new Person("John Smith", "John", "Smith", LocalDate.of(1975, 1, 12), GenderType.MALE, tree);
        personRepository.save(father);

        Person grandmother = new Person("Anne White", "Anne", "White", LocalDate.of(1950, 5, 1), GenderType.FEMALE, tree);
        personRepository.save(grandmother);

        Person grandfather = new Person("Adalbert Black", "Adalbert", "Black", LocalDate.of(1945, 1, 30), GenderType.MALE, tree);
        personRepository.save(grandfather);

        Person greatGrandmother = new Person("Clara Green", "Clara", "Green", LocalDate.of(1920, 4, 15), GenderType.FEMALE, tree);
        personRepository.save(greatGrandmother);

        Person greatGrandfather = new Person("Frank White", "Frank", "White", LocalDate.of(1918, 6, 25), GenderType.MALE, tree);
        personRepository.save(greatGrandfather);

        Person greatGrandmotherFather = new Person("Lucy Johnson", "Lucy", "Johnson", LocalDate.of(1922, 3, 10), GenderType.FEMALE, tree);
        personRepository.save(greatGrandmotherFather);

        Person greatGrandfatherFather = new Person("George Smith", "George", "Smith", LocalDate.of(1920, 2, 20), GenderType.MALE, tree);
        personRepository.save(greatGrandfatherFather);

        Family family = new Family();
        family.setFather(father);
        family.setMother(mother);
        Set<Person> children = new HashSet<>();
        children.add(child);
        family.setChildren(children);
        familyRepository.save(family);

        Family secondFamily = new Family();
        secondFamily.setFather(grandfather);
        secondFamily.setMother(grandmother);
        Set<Person> secondChildren = new HashSet<>();
        secondChildren.add(mother);
        secondFamily.setChildren(secondChildren);
        familyRepository.save(secondFamily);

        createPersonRelationship(child, father);
        createPersonRelationship(child, mother);
        createPersonRelationship(mother, grandmother);
        createPersonRelationship(mother, grandfather);

        createPersonRelationship2(child, father);
        createPersonRelationship2(child, mother);
        createPersonRelationship2(mother, grandmother);
        createPersonRelationship2(mother, grandfather);

        createPersonRelationship(grandmother, greatGrandmother);
        createPersonRelationship(grandmother, greatGrandfather);
        createPersonRelationship(father, greatGrandmotherFather);
        createPersonRelationship(father, greatGrandfatherFather);


        mockMvc.perform(get("/api/v1/genealogy/trees/{treeId}/families/{familyId}/persons/children", tree.getId(), family.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());


    }

    void createPersonRelationship(Person child, Person parent) {
        PersonRelationship personRelationship = new PersonRelationship();
        personRelationship.setChild(child);
        personRelationship.setPersonRelationshipType(PersonRelationshipType.BIOLOGICAL);
        parent.getChildren().add(personRelationship);
        personRepository.save(parent);
    }

    void createPersonRelationship2(Person child, Person parent) {
        PersonRelationship personRelationship = new PersonRelationship();
        personRelationship.setChild(parent);
        personRelationship.setPersonRelationshipType(PersonRelationshipType.BIOLOGICAL);
        child.getParents().add(personRelationship);
        personRepository.save(child);

    }
}
