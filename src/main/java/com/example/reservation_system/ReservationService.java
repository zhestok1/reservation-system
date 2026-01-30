package com.example.reservation_system;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReservationService { // Обработка бизнес логики

    private final Logger log = LoggerFactory.getLogger(ReservationService.class);
    private final ReservationRepository repository;

    public ReservationService(ReservationRepository repository) {
        this.repository = repository;
    }

    public Reservation getReservationByID(Long id) {
        ReservationEntity reservationEntity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found reservation with id=" + id));

        return toDomainReservation(reservationEntity);
    }


    public List<Reservation> findAllReservations() {
        List<ReservationEntity> allEntities = repository.findAll();
        List<Reservation> reservationList = allEntities.stream().map(this::toDomainReservation).toList();
        return reservationList;
    }

    public Reservation createReservation(Reservation reservationToCreate) {
        if (reservationToCreate.id() != null) {
            throw new IllegalArgumentException("Id should be empty!");
        }

        if (reservationToCreate.status() != null) {
            throw new IllegalArgumentException("Status should be empty!");
        }

        if (reservationToCreate.startDate().isAfter(reservationToCreate.endDate())) {
            throw new IllegalArgumentException("IllegalArgumentException");
        }

        var entityToSave = new ReservationEntity(
                null,
                reservationToCreate.userID(),
                reservationToCreate.roomID(),
                reservationToCreate.startDate(),
                reservationToCreate.endDate(),
                ReservationStatus.PENDING
        );
        var savedEntity = repository.save(entityToSave);
        return toDomainReservation(savedEntity);
    }

    public Reservation updateReservation(
            Long id,
            Reservation reservationToUpdate
    ) {

        var reservationEntity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found!"));
        if (reservationEntity.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException("You cannot modify that! status=" + reservationEntity.getStatus());
        }

        var reservationToSave = new ReservationEntity(
                reservationEntity.getId(),
                reservationToUpdate.userID(),
                reservationToUpdate.roomID(),
                reservationToUpdate.startDate(),
                reservationToUpdate.endDate(),
                ReservationStatus.PENDING
        );

        var updateReservation = repository.save(reservationToSave);
        return toDomainReservation(updateReservation);

    }

    @Transactional
    public void cancelledReservation(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("No found reservation by id!");
        }
        var reservation = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("No found reservation by id!"));
        if (reservation.getStatus().equals(ReservationStatus.APPROVED)) {
            throw new IllegalStateException("Call manager!");
        }
        if (reservation.getStatus().equals(ReservationStatus.CANCELLED)) {
            throw new IllegalStateException("Reservation was CANCELLED!");
        }
        repository.setStatus(id, ReservationStatus.CANCELLED);
        log.info("Successful CANCELLED!");
    }

    public Reservation approveReservation(Long id) {
        var reservationEntity = repository.findById(id).orElseThrow(()->new NoSuchElementException("Not found!"));
        if (reservationEntity.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException("You cannot modify that! status=" + reservationEntity.getStatus());
        }

        var isConflict = isReservationConflict(reservationEntity);
        if (isConflict) {
            throw new IllegalStateException("CONFLICT!");
        }


        reservationEntity.setStatus(ReservationStatus.APPROVED);
        repository.save(reservationEntity);

        return toDomainReservation(reservationEntity);
    }

    private boolean isReservationConflict(ReservationEntity reservation) {
        var allReservations = repository.findAll();
        for (ReservationEntity existingReservation : allReservations) {
            if (reservation.getId().equals(existingReservation.getId())) {
                continue;
            }
            if (!reservation.getRoomID().equals(existingReservation.getRoomID())) {
                continue;
            }
            if (!existingReservation.getStatus().equals(ReservationStatus.APPROVED)) {
                continue;
            }
            if (reservation.getStartDate().isBefore(existingReservation.getEndDate())
            && existingReservation.getStartDate().isBefore(reservation.getEndDate())) {
                return true;
            }
        }
        return false;
    }

    private Reservation toDomainReservation(ReservationEntity reservationEntity) {
        return new Reservation(
                reservationEntity.getId(),
                reservationEntity.getUserID(),
                reservationEntity.getRoomID(),
                reservationEntity.getStartDate(),
                reservationEntity.getEndDate(),
                reservationEntity.getStatus()
        );
    }
}
