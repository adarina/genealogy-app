package com.ada.genealogyapp.person.integration;

import com.ada.genealogyapp.citation.repository.CitationRepository;
import com.ada.genealogyapp.config.IntegrationTestConfig;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.type.StatusType;
import com.ada.genealogyapp.file.repository.FileRepository;
import com.ada.genealogyapp.person.relationship.PersonRelationship;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.repostitory.PersonRepository;
import com.ada.genealogyapp.person.type.GenderType;
import com.ada.genealogyapp.person.type.PersonRelationshipType;
import com.ada.genealogyapp.source.repository.SourceRepository;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


class PersonAncestorsViewIntegrationTest extends IntegrationTestConfig {

    @Autowired
    TreeRepository treeRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    FamilyRepository familyRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    SourceRepository sourceRepository;

    @Autowired
    CitationRepository citationRepository;

    @Autowired
    FileRepository fileRepository;

//    @Autowired
//    UserNeo4jRepository userNeo4jRepository;

    @BeforeEach
    void setUp() {

//        userNeo4jRepository.deleteAll();
        treeRepository.deleteAll();
        personRepository.deleteAll();
        familyRepository.deleteAll();
        eventRepository.deleteAll();
        sourceRepository.deleteAll();
        citationRepository.deleteAll();
        fileRepository.deleteAll();


    }

    @AfterEach
    void tearDown() {

//        treeRepository.deleteAll();
//        personRepository.deleteAll();
//        familyRepository.deleteAll();

//        treeRepository.deleteAll();
//        personRepository.deleteAll();
//        familyRepository.deleteAll();
//        eventRepository.deleteAll();
//        sourceRepository.deleteAll();
//        citationRepository.deleteAll();
//        fileRepository.deleteAll();
    }
//    Tree tree = new Tree(1L, "Tree", new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
//        tree.setUserNeo4j(user);
//        treeRepository.save(tree);


//    @Test
//    void shouldGetPersonAncestorsSuccessfully() throws Exception {
//
////        UserNeo4j user = new UserNeo4j();
////        user.setId(1L);
//
////        userNeo4jRepository.save(user);
////
////        Tree tree = new Tree();
////        tree.setName("Tree");
//////        tree.setUserNeo4j(user);
////        treeRepository.save(tree);
//        Tree tree = new Tree(1L, "Tree", new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
//        treeRepository.save(tree);
////
////        Family family = new Family("John Smith & Elizabeth Black", StatusType.MARRIED, null, null, null, tree);
////        familyRepository.save(family);
//
//        Person child = new Person("Amalia Smith", "Amalia", "Smith", LocalDate.of(2000, 12, 18), GenderType.FEMALE, new HashSet<>() ,tree);
//        personRepository.save(child);
//       Person mother = new Person("Elizabeth Black", "Elizabeth", "Black", LocalDate.of(1975, 7, 8), GenderType.FEMALE, new HashSet<>(), tree);
//        personRepository.save(mother);
//
//        Person father = new Person("John Smith", "John", "Smith", LocalDate.of(1975, 1, 12), GenderType.MALE, new HashSet<>(), tree);
//        personRepository.save(father);
//        Family secondFamily = new Family("John Smith & Elizabeth Black", StatusType.UNKNOWN, father, mother, List.of(child), tree);
//        familyRepository.save(secondFamily);
//
//
////        treeRepository.save(tree);
////
//
//
//        tree.setPersons(Set.of(child, mother, father));
//        tree.setFamilies(Set.of(secondFamily));
//        treeRepository.save(tree);
////
////        Person grandmother = new Person("Anne White", "Anne", "White", LocalDate.of(1950, 5, 1), GenderType.FEMALE, new HashSet<>(), tree);
////        personRepository.save(grandmother);
////
////        Person grandfather = new Person("Adalbert Black", "Adalbert", "Black", LocalDate.of(1945, 1, 30), GenderType.MALE, new HashSet<>(), tree);
////        personRepository.save(grandfather);
////
////        Person greatGrandmother = new Person("Clara Green", "Clara", "Green", LocalDate.of(1920, 4, 15), GenderType.FEMALE, new HashSet<>(), tree);
////        personRepository.save(greatGrandmother);
////
////        Person greatGrandfather = new Person("Frank White", "Frank", "White", LocalDate.of(1918, 6, 25), GenderType.MALE, new HashSet<>(), tree);
////        personRepository.save(greatGrandfather);
////
////        Person greatGrandmotherFather = new Person("Lucy Johnson", "Lucy", "Johnson", LocalDate.of(1922, 3, 10), GenderType.FEMALE, new HashSet<>(), tree);
////        personRepository.save(greatGrandmotherFather);
////
////        Person greatGrandfatherFather = new Person("George Smith", "George", "Smith", LocalDate.of(1920, 2, 20), GenderType.MALE, new HashSet<>(), tree);
////        personRepository.save(greatGrandfatherFather);
//
////        List<Person> children = new ArrayList<>();
////        children.add(child);
////        Family family = new Family("John Smith & Elizabeth Black", StatusType.MARRIED, father, mother, List.of(child), tree);
////        Family family = new Family("John Smith & Elizabeth Black", StatusType.MARRIED, null, null, null, tree);
////        familyRepository.save(family);
//
////        family.setFather(father);
////        family.setMother(mother);
////        family.setName("Elizabeth");
////        family.setStatus(StatusType.MARRIED);
////        List<Person> children = new ArrayList<>();
////        children.add(child);
////        family.setChildren(children);
////
////
////        family.setTree(tree);
////
////        Family secondFamily = new Family();
////        secondFamily.setFather(grandfather);
////        secondFamily.setMother(grandmother);
////        secondFamily.setTree(tree);
////        secondFamily.setName("Adalbert");
////        secondFamily.setStatus(StatusType.UNKNOWN);
////        List<Person> secondChildren = new ArrayList<>();
////        secondChildren.add(mother);
////        secondFamily.setChildren(secondChildren);
////        familyRepository.save(secondFamily);
////
////        secondFamily.setTree(tree);
////        familyRepository.save(secondFamily);
//
//        createPersonRelationship(child, father);
//        createPersonRelationship(child, mother);
////        createPersonRelationship(mother, grandmother);
////        createPersonRelationship(mother, grandfather);
////
////        createPersonRelationship(grandmother, greatGrandmother);
////        createPersonRelationship(grandmother, greatGrandfather);
////        createPersonRelationship(father, greatGrandmotherFather);
////        createPersonRelationship(father, greatGrandfatherFather);
//
////        mockMvc.perform(get("/api/v1/genealogy/trees/{treeId}/persons/{personId}/ancestors", tree.getId(), child.getId())
////                        .contentType(MediaType.APPLICATION_JSON))
////                .andDo(print())
////                .andExpect(status().isOk());
//    }

    @Test
    void lol() {

    }

     void createPersonRelationship(Person child, Person parent) {
        PersonRelationship personRelationship = new PersonRelationship();
        personRelationship.setChild(child);
        personRelationship.setRelationship(PersonRelationshipType.BIOLOGICAL);
        parent.getRelationships().add(personRelationship);
        personRepository.save(parent);
    }
}
