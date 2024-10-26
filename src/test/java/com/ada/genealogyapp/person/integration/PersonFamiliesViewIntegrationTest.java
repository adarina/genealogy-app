package com.ada.genealogyapp.person.integration;

import com.ada.genealogyapp.citation.repository.CitationRepository;
import com.ada.genealogyapp.config.IntegrationTestConfig;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.family.relationship.FamilyRelationship;
import com.ada.genealogyapp.person.relationship.PersonRelationship;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.person.repostitory.PersonRepository;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.type.GenderType;
import com.ada.genealogyapp.tree.model.Tree;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PersonFamiliesViewIntegrationTest extends IntegrationTestConfig {

    @Autowired
    TreeRepository treeRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    FamilyRepository familyRepository;

    @Autowired
    CitationRepository citationRepository;

    @BeforeEach
    void setUp() {

        treeRepository.deleteAll();
        personRepository.deleteAll();
        eventRepository.deleteAll();
        citationRepository.deleteAll();
        familyRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
//        treeRepository.deleteAll();
//        personRepository.deleteAll();
//        eventRepository.deleteAll();
//        citationRepository.deleteAll();
//        familyRepository.deleteAll();
    }

    @Test
    void shouldReturnAllFamiliesForPersonSuccessfully() throws Exception {
        // Step 1: Set up the tree
        Tree tree = new Tree();
        treeRepository.save(tree);

        // Step 2: Create persons
        Person person = new Person("John Smith", "John", "Smith", LocalDate.of(1975, 7, 18), GenderType.MALE, tree);
        personRepository.save(person);

        Person secondPerson = new Person("Mary Sue", "Mary", "Sue", LocalDate.of(1999, 12, 1), GenderType.FEMALE, tree);
        personRepository.save(secondPerson);

        // Step 3: Create families
        Family family1 = new Family();
        family1.setName("Rick Smith & Theodora Smith");
        family1.setFather(person); // Assuming person is the father
        family1.getChildren().add(secondPerson); // Add secondPerson as a child
        familyRepository.save(family1);

        Family family2 = new Family();
        family2.setName("John Smith & Mary Sue");
        family2.setFather(secondPerson); // Assuming secondPerson is the father
        family2.getChildren().add(person); // Add person as a child
        familyRepository.save(family2);

        // Step 4: Perform the GET request
        mockMvc.perform(get("/api/v1/genealogy/trees/{treeId}/persons/{personId}/families", tree.getId(), person.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.families").isArray())
                .andExpect(jsonPath("$.families.length()").value(2)) // Assuming the person is related to 2 families
                .andExpect(jsonPath("$.families[?(@.name == 'Rick Smith & Theodora Smith')].id").value(family1.getId().toString()))
                .andExpect(jsonPath("$.families[?(@.name == 'John Smith & Mary Sue')].id").value(family2.getId().toString()))
                .andExpect(jsonPath("$.families[?(@.name == 'Rick Smith & Theodora Smith')].father").value(person.getId().toString()))
                .andExpect(jsonPath("$.families[?(@.name == 'Rick Smith & Theodora Smith')].children.length()").value(1))
                .andExpect(jsonPath("$.families[?(@.name == 'John Smith & Mary Sue')].father").value(secondPerson.getId().toString()))
                .andExpect(jsonPath("$.families[?(@.name == 'John Smith & Mary Sue')].children.length()").value(1));
    }

    @Test
    void shouldReturnAllFamiliesForPersonSuccessfully1() throws Exception {
        Tree tree = new Tree();
        treeRepository.save(tree);

        Person child = new Person("Amalia Smith", "Amalia", "Smith", LocalDate.of(2000, 12, 18), GenderType.MALE, tree);
        personRepository.save(child);

        Person mother = new Person("Elizabeth Black", "Elizabeth", "Black", LocalDate.of(1975, 7, 8), GenderType.FEMALE, tree);
        personRepository.save(mother);

        Person father = new Person("John Smith", "John", "Smith", LocalDate.of(1975, 1, 12), GenderType.MALE, tree);
        personRepository.save(father);

        Person grandmother = new Person("Anne White", "Anne", "White", LocalDate.of(1950, 5, 1), GenderType.FEMALE, tree);
        personRepository.save(grandmother);

        Person grandfather = new Person("Adalbert Black", "Adalbert", "Black", LocalDate.of(1945, 1, 30), GenderType.MALE, tree);
        personRepository.save(grandfather);

        Family family = new Family();
        family.setFather(father);
        family.setMother(mother);
        Set<Person> children = new HashSet<>();
        children.add(child);
        family.setChildren(children);
        familyRepository.save(family);

        FamilyRelationship familyChildRelationship = new FamilyRelationship();
        familyChildRelationship.setFamily(family);
        child.setFamilies(Set.of(familyChildRelationship));

        FamilyRelationship familyMotherRelationship = new FamilyRelationship();
        familyMotherRelationship.setFamily(family);
        mother.setFamilies(Set.of(familyMotherRelationship));

        FamilyRelationship familyFatherRelationship = new FamilyRelationship();
        familyFatherRelationship.setFamily(family);
        father.setFamilies(Set.of(familyFatherRelationship));

        Family secondFamily = new Family();
        secondFamily.setFather(grandfather);
        secondFamily.setMother(grandmother);
        Set<Person> secondChildren = new HashSet<>();
        secondChildren.add(mother);
        secondFamily.setChildren(secondChildren);
        familyRepository.save(secondFamily);

        FamilyRelationship familyMother2Relationship = new FamilyRelationship();
        familyMother2Relationship.setFamily(secondFamily);
        mother.setFamilies(Set.of(familyMother2Relationship,familyMotherRelationship));

        FamilyRelationship familyGrandmotherRelationship = new FamilyRelationship();
        familyGrandmotherRelationship.setFamily(secondFamily);
        grandmother.setFamilies(Set.of(familyGrandmotherRelationship));

        FamilyRelationship familyGrandfatherRelationship = new FamilyRelationship();
        familyGrandfatherRelationship.setFamily(secondFamily);
        grandfather.setFamilies(Set.of(familyGrandfatherRelationship));

        PersonRelationship fatherPersonRelationship = new PersonRelationship();
        PersonRelationship fatherPersonRelationship2 = new PersonRelationship();
        fatherPersonRelationship.setChild(child);
        fatherPersonRelationship2.setChild(father);
        father.getChildren().add(fatherPersonRelationship);
        child.getParents().add(fatherPersonRelationship2);
        personRepository.save(father);
        personRepository.save(child);

        PersonRelationship motherPersonRelationship = new PersonRelationship();
        PersonRelationship motherPersonRelationship2 = new PersonRelationship();
        motherPersonRelationship.setChild(child);
        motherPersonRelationship2.setChild(mother);
        mother.getChildren().add(motherPersonRelationship);
        child.getParents().add(motherPersonRelationship2);
        personRepository.save(mother);
        personRepository.save(child);

        PersonRelationship grandmotherPersonRelationship = new PersonRelationship();
        PersonRelationship grandmotherPersonRelationship2 = new PersonRelationship();
        grandmotherPersonRelationship.setChild(mother);
        grandmotherPersonRelationship2.setChild(grandmother);
        grandmother.getChildren().add(grandmotherPersonRelationship);
        mother.getParents().add(grandmotherPersonRelationship2);
        personRepository.save(grandmother);
        personRepository.save(mother);

        PersonRelationship grandfatherPersonRelationship = new PersonRelationship();
        PersonRelationship grandfatherPersonRelationship2 = new PersonRelationship();
        grandfatherPersonRelationship.setChild(mother);
        grandfatherPersonRelationship2.setChild(grandfather);
        grandfather.getChildren().add(grandfatherPersonRelationship);
        mother.getParents().add(grandfatherPersonRelationship2);
        personRepository.save(grandfather);
        personRepository.save(mother);


        mockMvc.perform(get("/api/v1/genealogy/trees/{treeId}/persons/{personId}/families", tree.getId(), mother.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.families").isArray())
                .andExpect(jsonPath("$.families.length()").value(2));
    }


}
