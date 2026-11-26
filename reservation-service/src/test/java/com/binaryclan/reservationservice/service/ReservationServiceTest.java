package com.binaryclan.reservationservice.service;

import com.binaryclan.reservationservice.dto.ReservationDTO;
import com.binaryclan.reservationservice.model.Reservation;
import com.binaryclan.reservationservice.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    private Reservation reservation;
    private ReservationDTO reservationDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        reservation = new Reservation();
        reservation.setId(1);
        reservation.setCustomerId(101);
        reservation.setFlightId(202);
        reservation.setSeatId(303);
        reservation.setPaymentId(404);
        reservation.setCreatedDate(LocalDate.now());
//        reservation.setCreatedAt(LocalDateTime.now());

        reservationDTO = new ReservationDTO(
                1, 101, 202, 303, 404,
                reservation.getCreatedDate(), reservation.getCreatedAt()
        );
    }

    @Test
    void testCreateReservation() {
        when(reservationRepository.save(any())).thenReturn(reservation);

        ResponseEntity<ReservationDTO> response = reservationService.createReservation(reservationDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(101, response.getBody().getCustomerId());
        verify(reservationRepository).save(any());
    }

    @Test
    void testGetReservations() {
        when(reservationRepository.findAll()).thenReturn(Collections.singletonList(reservation));

        ResponseEntity<List<ReservationDTO>> response = reservationService.getReservations();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(reservationRepository).findAll();
    }

    @Test
    void testGetReservationById() {
        when(reservationRepository.findById(1)).thenReturn(Optional.of(reservation));

        ResponseEntity<ReservationDTO> response = reservationService.getReservation(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(101, response.getBody().getCustomerId());
        verify(reservationRepository).findById(1);
    }

    @Test
    void testGetReservationById_NotFound() {
        when(reservationRepository.findById(99)).thenReturn(Optional.empty());

        ResponseEntity<ReservationDTO> response = reservationService.getReservation(99);

        assertEquals(500, response.getStatusCodeValue());
        verify(reservationRepository).findById(99);
    }

    @Test
    void testDeleteReservation() {
        doNothing().when(reservationRepository).deleteById(1);

        ResponseEntity<String> response = reservationService.deleteReservation(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Reservation deleted successfully", response.getBody());
        verify(reservationRepository).deleteById(1);
    }

    @Test
    void testUpdateReservation() {
        when(reservationRepository.findById(1)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any())).thenReturn(reservation);

        ResponseEntity<ReservationDTO> response = reservationService.updateReservation(1, reservationDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(101, response.getBody().getCustomerId());
        verify(reservationRepository).findById(1);
        verify(reservationRepository).save(any());
    }

    @Test
    void testUpdateReservation_NotFound() {
        when(reservationRepository.findById(99)).thenReturn(Optional.empty());

        ResponseEntity<ReservationDTO> response = reservationService.updateReservation(99, reservationDTO);

        assertEquals(404, response.getStatusCodeValue());
        verify(reservationRepository).findById(99);
    }
}