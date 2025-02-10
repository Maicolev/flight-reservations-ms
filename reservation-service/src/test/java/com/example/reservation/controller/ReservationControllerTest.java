package com.example.reservation.controller;

import com.example.common.dto.ReservationRequest;
import com.example.reservation.service.PublishReservation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublishReservation publishReservation;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createReservation_Success() throws Exception {
        ReservationRequest request = new ReservationRequest(1L, "12A", "test@example.com");
        when(publishReservation.publishReservation(any())).thenReturn(true);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Reservation successful, data processed correctly."));
    }

    @Test
    void createReservation_Failure() throws Exception {
        ReservationRequest request = new ReservationRequest(1L, "12A", "test@example.com");
        when(publishReservation.publishReservation(any())).thenReturn(false);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("The reservation could not be processed. Review the data sent or availability of the chair."));
    }

    @Test
    void createReservation_InvalidRequest() throws Exception {
        String invalidRequest = "{\"flightId\": null, \"seatNumber\": \"A12\", \"email\": \"invalid\"}";

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }
}