package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.query.QueryResultProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.query.QueryResultProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class FamilyServiceImplTest {

//    @Mock
//    FamilyRepository familyRepository;
//
//    @InjectMocks
//    FamilyServiceImpl familyService;
//
//    String familyId;
//    Family family;
//
//    @BeforeEach
//    void setUp() {
//
//        familyId = String.valueOf(UUID.randomUUID());
//        family = new Family();
//        family.setId(familyId);
//    }
//
//    @Test
//    void findFamilyById_shouldReturnFamilyWhenFound() {
//
//        when(familyRepository.findById(familyId)).thenReturn(Optional.of(family));
//
//        Family result = familyService.findFamilyById(familyId);
//
//        assertNotNull(result);
//        assertEquals(familyId, result.getId());
//        verify(familyRepository).findById(familyId);
//    }
//
//    @Test
//    void findFamilyById_shouldThrowExceptionWhenNotFound() {
//
//        when(familyRepository.findById(familyId)).thenReturn(Optional.empty());
//
//        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class,
//                () -> familyService.findFamilyById(familyId));
//
//        assertEquals("Family not found with ID: " + familyId, exception.getMessage());
//        verify(familyRepository).findById(familyId);
//    }
//
//    @Test
//    void ensureFamilyExists_shouldNotThrowWhenFamilyExists() {
//
//        when(familyRepository.existsById(familyId)).thenReturn(true);
//
//        assertDoesNotThrow(() -> familyService.ensureFamilyExists(familyId));
//        verify(familyRepository).existsById(familyId);
//    }
//
//    @Test
//    void ensureFamilyExists_shouldThrowExceptionWhenFamilyDoesNotExist() {
//
//        when(familyRepository.existsById(familyId)).thenReturn(false);
//
//        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class,
//                () -> familyService.ensureFamilyExists(familyId));
//
//        assertEquals("Family not found with ID: " + familyId, exception.getMessage());
//        verify(familyRepository).existsById(familyId);
//    }
//
//    @Test
//    void saveFamily_shouldSaveFamilyAndLogMessage() {
//
//        when(familyRepository.save(family)).thenReturn(family);
//
//        familyService.saveFamily(family);
//
//        verify(familyRepository).save(family);
//
//    }
//
//    @Test
//    void deleteFamily_shouldDeleteFamilyAndLogMessage() {
//
//        familyService.deleteFamily(family);
//
//        verify(familyRepository).delete(family);
//    }

    @Mock
    private FamilyRepository familyRepository;

    @Mock
    private QueryResultProcessor queryResultProcessor;

    @Mock
    private FamilyService familyService;

    @InjectMocks
    private FamilyMotherManagementService familyMotherManagementService;

//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }


}

