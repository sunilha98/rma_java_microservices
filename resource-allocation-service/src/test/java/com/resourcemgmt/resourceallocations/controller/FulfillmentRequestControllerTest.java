package com.resourcemgmt.resourceallocations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.resourcemgmt.resourceallocations.dto.FulfillmentCreationRequestDTO;
import com.resourcemgmt.resourceallocations.dto.FulfillmentRequestDTO;
import com.resourcemgmt.resourceallocations.entity.*;
import com.resourcemgmt.resourceallocations.repository.FulfillmentRequestRepository;
import com.resourcemgmt.resourceallocations.repository.SkillSetRepository;
import com.resourcemgmt.resourceallocations.repository.TitleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FulfillmentRequestControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Mock
    private FulfillmentRequestRepository fulfillmentRequestRepository;

    @Mock
    private TitleRepository titleRepository;

    @Mock
    private SkillSetRepository skillSetRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private FulfillmentRequestController fulfillmentRequestController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(fulfillmentRequestController).build();
    }


    @Test
    void createFulfillmentRequest_ShouldReturnCreatedRequest() throws Exception {
        // 1. Setup complete test data
        FulfillmentCreationRequestDTO dto = new FulfillmentCreationRequestDTO();
        dto.setProjectId(1L);
        dto.setTitleId(1L);
        dto.setSkillsetIds(Arrays.asList(1L, 2L));
        dto.setLocationId(1L);
        dto.setShiftId(1L);
        dto.setExperience(new BigDecimal("5.0")); // Use String constructor for precise decimal
        dto.setPositions(2);
        dto.setExpectedClosure(LocalDate.now().plusMonths(1));
        dto.setNotes("Test notes");

        // 2. Setup complete mock entities
        Title title = new Title();
        title.setId(1L);
        title.setName("Developer");

        Skillset skill1 = new Skillset();
        Skillset skill2 = new Skillset();

        FulfillmentRequest savedRequest = new FulfillmentRequest();
        savedRequest.setId(UUID.randomUUID());
        savedRequest.setProjectId(dto.getProjectId());
        savedRequest.setTitle(title);
        savedRequest.setSkillsets(Arrays.asList(skill1, skill2));
        savedRequest.setStatus(FulfillmentRequest.Status.OPEN);
        savedRequest.setExperience(dto.getExperience());
        savedRequest.setPositions(dto.getPositions());

        // 3. Configure all mock behaviors
        when(titleRepository.findById(1L)).thenReturn(Optional.of(title));
        when(skillSetRepository.findAllById(anyList())).thenReturn(Arrays.asList(skill1, skill2));
        when(fulfillmentRequestRepository.save(any(FulfillmentRequest.class))).thenReturn(savedRequest);

        // 4. Mock external service response
        Map<String, String> mockProjectResponse = new HashMap<>();
        mockProjectResponse.put("name", "Test Project");
        when(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class))
        ).thenReturn(ResponseEntity.ok(mockProjectResponse));

        // 5. Execute request with ALL required headers and proper content
        mockMvc.perform(post("/fulfillment-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Bearer-Token", "valid-token") // Ensure required header
                        .content(objectMapper.writeValueAsString(dto))) // Proper JSON serialization
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.projectId").value(1L))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    void getAllRequests_ShouldReturnFulfillmentRequests() throws Exception {
        // 1. Setup complete test data with all required fields
        Title title = new Title();
        title.setId(1L);
        title.setName("Developer");
        title.setIsActive(true);

        Skillset skill1 = new Skillset();
        skill1.setId(1L);
        skill1.setName("Java");
        skill1.setCategory("Technical");

        Skillset skill2 = new Skillset();
        skill2.setId(2L);
        skill2.setName("Spring");
        skill2.setCategory("Technical");

        FulfillmentRequest request = new FulfillmentRequest();
        request.setId(UUID.randomUUID());
        request.setProjectId(1L);
        request.setTitle(title);  // Make sure title is set
        request.setSkillsets(Arrays.asList(skill1, skill2));
        request.setLocationId(1L);
        request.setShiftId(1L);
        request.setStatus(FulfillmentRequest.Status.OPEN);
        request.setExpectedClosure(LocalDate.now().plusMonths(1));
        request.setNotes("Urgent requirement");
        request.setExperience(new BigDecimal("5.0"));
        request.setPositions(2);

        // 2. Mock repository response
        when(fulfillmentRequestRepository.findAll()).thenReturn(Arrays.asList(request));

        // 3. Mock external service responses - use exact URL matches
        when(restTemplate.exchange(
                eq("http://localhost:8080/api/locations/1"),  // Exact URL
                any(HttpMethod.class),
                any(HttpEntity.class),
                eq(Map.class))
        ).thenReturn(ResponseEntity.ok(Map.of("name", "New York")));

        when(restTemplate.exchange(
                eq("http://localhost:8080/api/shifts/1"),  // Exact URL
                any(HttpMethod.class),
                any(HttpEntity.class),
                eq(Map.class))
        ).thenReturn(ResponseEntity.ok(Map.of("name", "Day Shift")));

        when(restTemplate.exchange(
                eq("http://localhost:8080/api/projects/1"),  // Exact URL
                any(HttpMethod.class),
                any(HttpEntity.class),
                eq(Map.class))
        ).thenReturn(ResponseEntity.ok(Map.of("name", "E-Commerce Platform")));

        // 4. Execute and verify with detailed response printing
        mockMvc.perform(get("/fulfillment-requests")
                        .header("X-Bearer-Token", "test-token"))
                .andDo(print())  // Print full request/response for debugging
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].projectName").value("E-Commerce Platform"))
                .andExpect(jsonPath("$[0].title").value("Developer"))  // This was failing
                .andExpect(jsonPath("$[0].skills.length()").value(2))
                .andExpect(jsonPath("$[0].location").value("New York"))
                .andExpect(jsonPath("$[0].shift").value("Day Shift"))
                .andExpect(jsonPath("$[0].status").value("OPEN"))
                .andExpect(jsonPath("$[0].positions").value(2));
    }

    @Test
    void updateStatusAndNotes_ShouldUpdateRequest() throws Exception {
        // 1. Setup complete test data
        UUID requestId = UUID.randomUUID();

        Title title = new Title();
        FulfillmentRequest existingRequest = new FulfillmentRequest();
        existingRequest.setId(requestId);
        existingRequest.setProjectId(1L);
        existingRequest.setTitle(title);
        existingRequest.setStatus(FulfillmentRequest.Status.OPEN);
        existingRequest.setNotes("Original notes");

        Map<String, Object> updates = Map.of(
                "status", "IN_PROGRESS",
                "notes", "Updated notes"
        );

        // 2. Mock repository responses
        when(fulfillmentRequestRepository.findById(requestId))
                .thenReturn(Optional.of(existingRequest));

        when(fulfillmentRequestRepository.save(any(FulfillmentRequest.class)))
                .thenAnswer(invocation -> {
                    FulfillmentRequest saved = invocation.getArgument(0);
                    saved.setStatus(FulfillmentRequest.Status.IN_PROGRESS);
                    saved.setNotes("Updated notes");
                    return saved;
                });

        // 3. Mock external service response
        when(restTemplate.exchange(
                eq("http://localhost:8080/api/projects/1"),
                any(HttpMethod.class),
                any(HttpEntity.class),
                eq(Map.class))
        ).thenReturn(ResponseEntity.ok(Map.of("name", "Test Project")));

        // 4. Execute and verify the empty response
        mockMvc.perform(put("/fulfillment-requests/{id}", requestId)
                        .header("X-Bearer-Token", "test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updates)))
                .andDo(print())  // Print response for debugging
                .andExpect(status().isOk())
                .andExpect(content().string(""));  // Expect empty response

        // 5. Verify the save operation
        verify(fulfillmentRequestRepository).save(argThat(req ->
                req.getStatus() == FulfillmentRequest.Status.IN_PROGRESS &&
                        "Updated notes".equals(req.getNotes())
        ));
    }
}