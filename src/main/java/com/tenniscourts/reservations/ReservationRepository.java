package com.tenniscourts.reservations;

import com.tenniscourts.guests.Guest;
import com.tenniscourts.schedules.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByScheduleAndGuest(Schedule schedule, Guest guest);

    List<Reservation> findBySchedule_Id(Long scheduleId);

    Long countByScheduleAndReservationStatus(Schedule schedule, ReservationStatus reservationStatus);

    List<Reservation> findByReservationStatusAndSchedule_StartDateTimeGreaterThanEqualAndSchedule_EndDateTimeLessThanEqual(ReservationStatus reservationStatus, LocalDateTime startDateTime, LocalDateTime endDateTime);

//    List<Reservation> findByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqualAndTennisCourt(LocalDateTime startDateTime, LocalDateTime endDateTime, TennisCourt tennisCourt);
}
