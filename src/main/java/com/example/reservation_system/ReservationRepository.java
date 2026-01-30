package com.example.reservation_system;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

//    @Query(value = "select * from reservations r where r.status = :status", nativeQuery = true)
//    List<ReservationEntity> findAllByStatusIs(ReservationStatus status);
//
//    @Query("select r from ReservationEntity r where r.roomId = :roomID")
//    List<ReservationEntity> findAllByRoomId(@Param("roomId") Long roomId);
//
//    @Transactional
//    @Modifying
//    @Query("update ReservationEntity r set r.userId = :userID, " +
//            "r.roomId = :roomID, " +
//            "r.startDate = :startDate, +" +
//            "r.endDate = :endDate," +
//            "r.status = :status where r.id = :id")
//    int updateAllFields(
//            @Param("id") Long id,
//            @Param("userID") Long userId,
//            @Param("roomID") Long roomId,
//            @Param("startDate")LocalDate startDate,
//            @Param("endDate") LocalDate endDate,
//            @Param("status") ReservationStatus status
//    );

    @Modifying
    @Query("update ReservationEntity r set r.status = :status where r.id = :id")
    public void setStatus(@Param("id") Long id, @Param("status") ReservationStatus status);
}
