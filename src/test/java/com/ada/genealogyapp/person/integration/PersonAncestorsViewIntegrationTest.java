package com.ada.genealogyapp.person.integration;

import com.ada.genealogyapp.config.IntegrationTestConfig;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.relationship.ChildRelationship;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.repostitory.PersonRepository;
import com.ada.genealogyapp.person.type.GenderType;
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

    @BeforeEach
    void setUp() {

        treeRepository.deleteAll();
        personRepository.deleteAll();
        familyRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {

        treeRepository.deleteAll();
        personRepository.deleteAll();
        familyRepository.deleteAll();
    }

    @Test
    void shouldGetPersonAncestorsSuccessfully() throws Exception {

        Tree tree = new Tree();
        treeRepository.save(tree);

        Person child = new Person("Amalia", "Smith", LocalDate.of(2000, 12, 18), GenderType.MALE, tree);
        personRepository.save(child);

        Person mother = new Person("Elizabeth", "Black", LocalDate.of(1975, 7, 8), GenderType.FEMALE, tree);
        personRepository.save(mother);

        Person father = new Person("John", "Smith", LocalDate.of(1975, 1, 12), GenderType.MALE, tree);
        personRepository.save(father);

        Person grandmother = new Person("Anne", "White", LocalDate.of(1950, 5, 1), GenderType.FEMALE, tree);
        personRepository.save(grandmother);

        Person grandfather = new Person("Adalbert", "Black", LocalDate.of(1945, 1, 30), GenderType.MALE, tree);
        personRepository.save(grandfather);

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

        ChildRelationship fatherChildRelationship = new ChildRelationship();
        fatherChildRelationship.setChild(child);
        father.getChildren().add(fatherChildRelationship);
        personRepository.save(father);

        ChildRelationship motherChildRelationship = new ChildRelationship();
        motherChildRelationship.setChild(child);
        mother.getChildren().add(motherChildRelationship);
        personRepository.save(mother);

        ChildRelationship grandmotherChildRelationship = new ChildRelationship();
        grandmotherChildRelationship.setChild(mother);
        grandmother.getChildren().add(grandmotherChildRelationship);
        personRepository.save(grandmother);

        ChildRelationship grandfatherChildRelationship = new ChildRelationship();
        grandfatherChildRelationship.setChild(mother);
        grandfather.getChildren().add(grandfatherChildRelationship);
        personRepository.save(grandfather);

        mockMvc.perform(get("/api/v1/genealogy/trees/{treeId}/persons/{personId}/ancestors", tree.getId(), child.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }
}
