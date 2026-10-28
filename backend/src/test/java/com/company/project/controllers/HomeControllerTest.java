package com.company.project.controllers;

import com.company.project.entity.Greeting;
import com.company.project.repository.GreetingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("HomeController Unit Tests")
class HomeControllerTest {

    @Mock
    private GreetingRepository greetingRepository;

    @InjectMocks
    private HomeController homeController;

    @Test
    @DisplayName("showHome trả về Greeting khi tìm thấy trong database (id = 1)")
    void showHome_WhenGreetingExists_ShouldReturnGreeting() {
        // Arrangee
        Greeting mockGreeting = new Greeting(1, "Hello from Docker!");
        when(greetingRepository.findById(1)).thenReturn(Optional.of(mockGreeting));

        // Act
        Greeting result = homeController.showHome(null, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Hello from Docker!", result.getName());
        verify(greetingRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("showHome trả về greeting mặc định khi không tìm thấy record")
    void showHome_WhenGreetingDoesNotExist_ShouldReturnDefaultGreeting() {
        // Arrange
        when(greetingRepository.findById(1)).thenReturn(Optional.empty());

        // Act
        Greeting result = homeController.showHome(null, null);

        // Assert
        assertNotNull(result);
        assertEquals("Not Found 😕", result.getName());
        verify(greetingRepository, times(1)).findById(1);
    }
}

