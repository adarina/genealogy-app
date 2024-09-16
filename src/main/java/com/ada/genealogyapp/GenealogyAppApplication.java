package com.ada.genealogyapp;

import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.person.Gender;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.relationship.ChildRelationship;
import com.ada.genealogyapp.person.relationship.ChildRelationshipType;
import com.ada.genealogyapp.person.relationship.FamilyRelationshipType;
import com.ada.genealogyapp.person.repostitory.PersonRepository;
import com.ada.genealogyapp.person.service.LineageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableNeo4jRepositories
@Slf4j
public class GenealogyAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenealogyAppApplication.class, args);
    }

    @Bean
    CommandLineRunner demo(PersonRepository personRepository, FamilyRepository familyRepository) {
        return args -> {



            personRepository.deleteAll();
            familyRepository.deleteAll();



            Person pawelZablocki = new Person("Paweł", "Zabłocki", LocalDate.of(1706, 1, 1), Gender.MALE);
            Person lucjaZablocka = new Person("Łucja", "Zabłocka", null, Gender.FEMALE);

            Person zofiaZablocka = new Person("Zofia","Zabłocka", LocalDate.of(1730, 1, 1), Gender.FEMALE);
            Person malgorzataZablocka = new Person("Małgorzata", "Zabłocka", LocalDate.of(1745, 7, 18), Gender.FEMALE);

            pawelZablocki.addPartner(lucjaZablocka, FamilyRelationshipType.UNKNOWN);
            lucjaZablocka.addPartner(pawelZablocki, FamilyRelationshipType.UNKNOWN);

            personRepository.save(pawelZablocki);
            personRepository.save(lucjaZablocka);

            pawelZablocki.addChild(zofiaZablocka, ChildRelationshipType.BIOLOGICAL);
            lucjaZablocka.addChild(zofiaZablocka, ChildRelationshipType.BIOLOGICAL);

            pawelZablocki.addChild(malgorzataZablocka, ChildRelationshipType.BIOLOGICAL);
            lucjaZablocka.addChild(malgorzataZablocka, ChildRelationshipType.BIOLOGICAL);

            personRepository.save(malgorzataZablocka);
            personRepository.save(zofiaZablocka);

            Family rodzinaZablockich = new Family();
            rodzinaZablockich.addMember(pawelZablocki);
            rodzinaZablockich.addMember(lucjaZablocka);
            rodzinaZablockich.addMember(zofiaZablocka);
            rodzinaZablockich.addMember(malgorzataZablocka);
            familyRepository.save(rodzinaZablockich);

            log.info("Rodzina Zabłockich: " + rodzinaZablockich);

            Person kazimierzBrzozowski = new Person("Kazimierz", "Brzozowski", LocalDate.of(1736, 1, 1), Gender.MALE);
            personRepository.save(kazimierzBrzozowski);

            kazimierzBrzozowski.addPartner(zofiaZablocka, FamilyRelationshipType.MARRIED);
            zofiaZablocka.addPartner(kazimierzBrzozowski, FamilyRelationshipType.MARRIED);

            Person franciszekBrzozowski = new Person("Franciszek", "Brzozowski", LocalDate.of(1769, 3, 25), Gender.MALE);

            kazimierzBrzozowski.addChild(franciszekBrzozowski, ChildRelationshipType.BIOLOGICAL);
            zofiaZablocka.addChild(franciszekBrzozowski, ChildRelationshipType.BIOLOGICAL);

            personRepository.save(franciszekBrzozowski);

            Family rodzinaBrzozowskich = new Family();
            rodzinaBrzozowskich.addMember(kazimierzBrzozowski);
            rodzinaBrzozowskich.addMember(zofiaZablocka);
            rodzinaBrzozowskich.addMember(franciszekBrzozowski);
            familyRepository.save(rodzinaBrzozowskich);

            log.info("Rodzina Brzozowskich: " + rodzinaBrzozowskich);
            Set<ChildRelationship> children = franciszekBrzozowski.getChildren();
            if (children != null && !children.isEmpty()) {
                String childrenDetails = children.stream()
                        .map(ChildRelationship::toString)
                        .collect(Collectors.joining(", "));
                log.info("Children of Franciszek: " + childrenDetails);
            } else {
                log.info("Franciszek has no children.");
            }


            Person katarzynaZablocka = new Person("Katarzyna", "Zabłocka", LocalDate.of(1776, 4, 28), Gender.FEMALE);
            personRepository.save(katarzynaZablocka);

            malgorzataZablocka.addChild(katarzynaZablocka, ChildRelationshipType.BIOLOGICAL);

            Family rodzinaZablockich2 = new Family();
            rodzinaZablockich2.addMember(malgorzataZablocka);
            rodzinaZablockich2.addMember(katarzynaZablocka);
            familyRepository.save(rodzinaZablockich2);

            log.info("Rodzina Zabłockich 2: " + rodzinaZablockich2);
            Set<ChildRelationship> children2 = malgorzataZablocka.getChildren();
            if (children2 != null && !children2.isEmpty()) {
                String childrenDetails = children2.stream()
                        .map(ChildRelationship::toString)
                        .collect(Collectors.joining(", "));
                log.info("Children of Małgorzata: " + childrenDetails);
            } else {
                log.info("Małgorzata has no children.");
            }

            franciszekBrzozowski.addPartner(katarzynaZablocka, FamilyRelationshipType.MARRIED);
            katarzynaZablocka.addPartner(franciszekBrzozowski, FamilyRelationshipType.MARRIED);

            Person mariannaBrzozowska = new Person("Marianna", "Brzozowska", LocalDate.of(1798, 7, 28), Gender.FEMALE);
            personRepository.save(mariannaBrzozowska);

            katarzynaZablocka.addChild(mariannaBrzozowska, ChildRelationshipType.BIOLOGICAL);
            franciszekBrzozowski.addChild(mariannaBrzozowska, ChildRelationshipType.BIOLOGICAL);

            Family rodzinaBrzozowskich2 = new Family();
            rodzinaBrzozowskich2.addMember(franciszekBrzozowski);
            rodzinaBrzozowskich2.addMember(katarzynaZablocka);
            rodzinaBrzozowskich2.addMember(mariannaBrzozowska);
            familyRepository.save(rodzinaBrzozowskich2);

            log.info("Rodzina Brzozowskich 2: " + rodzinaBrzozowskich2);
            Set<ChildRelationship> children3 = franciszekBrzozowski.getChildren();
            if (children3 != null && !children3.isEmpty()) {
                String childrenDetails = children3.stream()
                        .map(ChildRelationship::toString)
                        .collect(Collectors.joining(", "));
                log.info("Children of Franciszek: " + childrenDetails);
            } else {
                log.info("Franciszek has no children.");
            }

            LineageService lineageService = new LineageService(personRepository);
            lineageService.printAncestorsTree("Marianna", "Brzozowska");

            pawelZablocki.addPartner(mariannaBrzozowska, FamilyRelationshipType.MARRIED);
            mariannaBrzozowska.addPartner(pawelZablocki, FamilyRelationshipType.MARRIED);

            personRepository.save(pawelZablocki);
            personRepository.save(lucjaZablocka);

        };
    }
}
