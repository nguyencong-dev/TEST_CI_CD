package com.company.project.controllers;

import com.company.project.entity.Greeting;
import com.company.project.repository.GreetingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("HomeController MockMvc Web Tests")
class HomeControllerWebTest {

    private MockMvc mockMvc;

    @Mock
    private GreetingRepository greetingRepository;

    @InjectMocks
    private HomeController homeController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
    }

    @Test
    @DisplayName("GET / trả về HTTP 200 và dữ liệu JSON Greeting khi tìm thấy")
    void getRoot_WhenFound_ShouldReturnJson() throws Exception {
        when(greetingRepository.findById(1)).thenReturn(Optional.of(new Greeting(1, "Docker Spring React")));

        mockMvc.perform(get("/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Docker Spring React"));
    }

    @Test
    @DisplayName("GET / trả về HTTP 200 và fallback text khi không tìm thấy record")
    void getRoot_WhenNotFound_ShouldReturnDefaultMessage() throws Exception {
        when(greetingRepository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Not Found 😕"));
    }

    @Test
    void getGreetings_ReturnsSortedDatabaseRecords() throws Exception {
        when(greetingRepository.findAll()).thenReturn(Arrays.asList(
                new Greeting(3, "MariaDB"), new Greeting(1, "Docker"), new Greeting(2, "Spring Boot")));

        mockMvc.perform(get("/greetings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Docker"))
                .andExpect(jsonPath("$[1].name").value("Spring Boot"))
                .andExpect(jsonPath("$[2].name").value("MariaDB"));
    }

    @Test
    void getGreetingById_ReturnsRecordOr404() throws Exception {
        when(greetingRepository.findById(2)).thenReturn(Optional.of(new Greeting(2, "Spring Boot")));
        when(greetingRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/greetings/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Spring Boot"));
        mockMvc.perform(get("/greetings/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getSummary_ReturnsDatabaseCount() throws Exception {
        when(greetingRepository.count()).thenReturn(3L);

        mockMvc.perform(get("/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.service").value("Greeting API"))
                .andExpect(jsonPath("$.database").value("MariaDB"))
                .andExpect(jsonPath("$.totalGreetings").value(3));
    }
}

