package com.stocktrack.stocktrack.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stocktrack.stocktrack.DTO.Request.AlertRequestDTO;
import com.stocktrack.stocktrack.DTO.Response.AlertResponseDTO;
import com.stocktrack.stocktrack.Entity.Enum.ConditionType;
import com.stocktrack.stocktrack.Service.AlertService;

import com.stocktrack.stocktrack.Service.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.stocktrack.stocktrack.Service.JwtService;
import org.springframework.security.core.userdetails.UserDetailsService;

@WebMvcTest(AlertController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private AlertService alertService;

    @Test
    @DisplayName("GET /api/alerts/{id}: Should return 200 OK and alert payload")
    void getAlertById_ReturnsOk() throws Exception {

        AlertResponseDTO responseDto = new AlertResponseDTO();
        responseDto.setId(10L);

        when(alertService.getAlertById(eq(10L), any()))
                .thenReturn(responseDto);

        mockMvc.perform(get("/api/alerts/{id}", 10L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L));

        verify(alertService, times(1))
                .getAlertById(eq(10L), any());
    }

    @Test
    @DisplayName("POST /api/alerts: Should return 201 Created on valid input")
    void addAlert_ValidRequest_ReturnsCreated() throws Exception {

        AlertRequestDTO requestDto = new AlertRequestDTO();
        requestDto.setTicker("AAPL");
        requestDto.setName("My Apple Alert");
        requestDto.setTargetPrice(150.0);
        requestDto.setConditionType(ConditionType.ABOVE);

        AlertResponseDTO responseDto = new AlertResponseDTO();
        responseDto.setId(1L);

        when(alertService.addAlert(any(AlertRequestDTO.class), any()))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/alerts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("DELETE /api/alerts/{id}: Should return 204 No Content")
    void deleteAlertById_ReturnsNoContent() throws Exception {

        mockMvc.perform(delete("/api/alerts/{id}", 10L))
                .andExpect(status().isNoContent());

        verify(alertService, times(1))
                .deleteAlertById(eq(10L), any());
    }
}