package com.binaryclan.reservationviewerservice.service;

import com.binaryclan.reservationviewerservice.dto.ReservationDTO;
import com.binaryclan.reservationviewerservice.model.Reservation;
import com.binaryclan.reservationviewerservice.repository.ReservationViewerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationViewerServiceTest {

    @InjectMocks
    private ReservationViewerService reservationViewerService;

    @Mock
    private ReservationViewerRepository reservationViewerRepository;

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
    void testGetReservations() {
        when(reservationViewerRepository.findAll()).thenReturn(Collections.singletonList(reservation));

        List<ReservationDTO> result = reservationViewerService.getReservations();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(101, result.get(0).getCustomerId());
        verify(reservationViewerRepository).findAll();
    }

    @Test
    void testGetReservationById() {
        when(reservationViewerRepository.findById(1)).thenReturn(Optional.of(reservation));

        ReservationDTO result = reservationViewerService.getReservation(1);

        assertNotNull(result);
        assertEquals(101, result.getCustomerId());
        verify(reservationViewerRepository).findById(1);
    }

    @Test
    void testGetReservationsByCustomerId() {
        when(reservationViewerRepository.findByCustomerId(101)).thenReturn(Collections.singletonList(reservation));

        List<ReservationDTO> result = reservationViewerService.getReservationsByCustomerId(101);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(101, result.get(0).getCustomerId());
        verify(reservationViewerRepository).findByCustomerId(101);
    }

    @Test
    void testGetReservationsByFlightId() {
        when(reservationViewerRepository.findByFlightId(202)).thenReturn(Collections.singletonList(reservation));

        List<ReservationDTO> result = reservationViewerService.getReservationsByFlightId(202);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(202, result.get(0).getFlightId());
        verify(reservationViewerRepository).findByFlightId(202);
    }

    @Test
    void testCreateReservation() {
        doNothing().when(reservationViewerRepository).save(any());

        reservationViewerService.createReservation(reservationDTO);

        verify(reservationViewerRepository).save(any());
    }

    @Test
    void testDeleteReservation() {
        doNothing().when(reservationViewerRepository).deleteById(1);

        reservationViewerService.deleteReservation(1);

        verify(reservationViewerRepository).deleteById(1);
    }

    @Test
    void testUpdateReservation() {
        when(reservationViewerRepository.findById(1)).thenReturn(Optional.of(reservation));
        when(reservationViewerRepository.save(any())).thenReturn(reservation);

        reservationViewerService.updateReservation(1, reservationDTO);

        verify(reservationViewerRepository).findById(1);
        verify(reservationViewerRepository).save(any());
    }
}