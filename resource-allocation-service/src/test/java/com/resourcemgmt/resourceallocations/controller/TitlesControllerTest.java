package com.resourcemgmt.resourceallocations.controller;

import com.resourcemgmt.resourceallocations.entity.Title;
import com.resourcemgmt.resourceallocations.repository.TitleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TitlesControllerTest {

    @Mock
    private TitleRepository titleRepository;

    @InjectMocks
    private TitlesController titlesController;

    private Title title1;
    private Title title2;

    @BeforeEach
    void setUp() {
        title1 = new Title();
        title1.setId(1L);
        title1.setName("Software Engineer");

        title2 = new Title();
        title2.setId(2L);
        title2.setName("Product Manager");
    }

    @Test
    void getAllTitles_ReturnsListOfTitles() {
        // Arrange
        List<Title> mockTitles = Arrays.asList(title1, title2);
        when(titleRepository.findAll()).thenReturn(mockTitles);

        // Act
        List<Title> result = titlesController.getAllTitles();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Software Engineer", result.get(0).getName());
        assertEquals("Product Manager", result.get(1).getName());
        verify(titleRepository, times(1)).findAll();
    }

    @Test
    void getAllTitles_ReturnsEmptyListWhenNoTitlesExist() {
        // Arrange
        when(titleRepository.findAll()).thenReturn(List.of());

        // Act
        List<Title> result = titlesController.getAllTitles();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(titleRepository, times(1)).findAll();
    }

    @Test
    void getAllTitles_RepositoryThrowsException_PropagatesException() {
        // Arrange
        when(titleRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> titlesController.getAllTitles());
        verify(titleRepository, times(1)).findAll();
    }
}