package com.example.processing.controller;

import com.example.common.dto.ReservationRequest;
import com.example.common.model.Reservation;
import com.example.processing.service.ReservationProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationProcessingController.class)
class ReservationProcessingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationProcessor processor;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void processReservations_Success() throws Exception {
        ReservationRequest request = new ReservationRequest(1L, "12A", "test@example.com");

        mockMvc.perform(post("/api/processing")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(processor).processPendingReservations(any());
    }

    @Test
    void getConfirmedReservations_Success() throws Exception {
        List<Reservation> reservations = Collections.singletonList(new Reservation());
        when(processor.getConfirmedReservations(anyLong())).thenReturn(reservations);

        mockMvc.perform(get("/api/processing/confirmed")
                        .param("flightId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void cancelReservation_Success() throws Exception {
        mockMvc.perform(delete("/api/processing/1"))
                .andExpect(status().isOk());

        verify(processor).cancelReservation(1L);
    }
}