package com.example.reservation_system.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Table(name = "reservations")
@Entity
public class ReservationEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userID;

    @Column(name = "room_id", nullable = false)
    private Long roomID;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReservationStatus status;

    public ReservationEntity(Long id, Long userID, Long roomID, LocalDate startDat, LocalDate endDate, ReservationStatus status) {
        this.id = id;
        this.userID = userID;
        this.roomID = roomID;
        this.startDate = startDat;
        this.endDate = endDate;
        this.status = status;
    }

    public ReservationEntity() {}

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getRoomID() {
        return roomID;
    }

    public void setRoomID(Long roomID) {
        this.roomID = roomID;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDat) {
        this.startDate = startDat;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
