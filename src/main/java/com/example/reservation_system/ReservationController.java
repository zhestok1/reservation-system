package com.example.reservation_system;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/reservation")
public class ReservationController { // Обработка запросов

    private final ReservationService reservationService;

    private static final Logger log =
            LoggerFactory.getLogger(ReservationController.class);

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationByID(
            @PathVariable("id") Long id
    ) {
        log.info("called getReservationByID, id=" + id);
        try {
            return ResponseEntity.status(HttpStatus.OK).body(reservationService.getReservationByID(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404)
                    .build();
        }

    }

    @GetMapping()
    public ResponseEntity<List<Reservation>> getAllReservationByID() {
        log.info("called getAllReservationByID");
        return ResponseEntity.status(HttpStatus.OK).body(reservationService.findAllReservations());
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(
           @RequestBody Reservation reservationToCreate // Jackson framework JSON to Java Object
    ) {
        log.info("called createReservation");
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.createReservation(reservationToCreate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(
            @PathVariable("id") Long id,
            @RequestBody Reservation reservationToUpdate
    ) {
        log.info("called updateReservation id={}, reservationToUpdate={}", id, reservationToUpdate);
        var update = reservationService.updateReservation(id, reservationToUpdate);
        return ResponseEntity.ok((Reservation) update);
    }

    @DeleteMapping("/{id}/cancelled")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable("id") Long id
    ) {
        log.info("called deleteReservation id=" + id);
        try {
            reservationService.cancelledReservation(id);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404)
                    .build();
        }
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Reservation> approveReservation(
            @PathVariable("id") Long id
    ) {
        log.info("called approveReservation");
        var reservation = reservationService.approveReservation(id);
        return ResponseEntity.ok(reservation);
    }

}
