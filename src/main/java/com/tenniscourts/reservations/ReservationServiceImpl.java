package com.tenniscourts.reservations;

import com.tenniscourts.exceptions.AlreadyExistsEntityException;
import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.Guest;
import com.tenniscourts.guests.GuestRepository;
import com.tenniscourts.schedules.Schedule;
import com.tenniscourts.schedules.ScheduleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final GuestRepository guestRepository;
    private final ScheduleRepository scheduleRepository;
    private final ReservationMapper reservationMapper;

    public ReservationDTO bookReservation(CreateReservationRequestDTO createReservationRequestDTO) {
        log.debug("[SERVICE] Book reservation");
        Guest guest = guestRepository.findById(createReservationRequestDTO.getGuestId()).orElseThrow(() -> {
            throw new EntityNotFoundException("Guest not found");
        });
        Schedule schedule = scheduleRepository.findById(createReservationRequestDTO.getScheduleId()).orElseThrow(() -> {
            throw new EntityNotFoundException("Schedule not found");
        });
        if (reservationRepository.countByScheduleAndReservationStatus(schedule, ReservationStatus.READY_TO_PLAY) > 0) {
            throw new AlreadyExistsEntityException("This schedule was book someone else. Please reconsider");
        }
        Reservation newReservation = Reservation.builder()
                .guest(guest)
                .schedule(schedule)
                .value(BigDecimal.valueOf(10))
                .reservationStatus(ReservationStatus.READY_TO_PLAY)
                .build();
        return reservationMapper.entityToDto(reservationRepository.save(newReservation));
    }

    public ReservationDTO findReservation(Long reservationId) {
        log.debug("[SERVICE] Find reservation by id: [{}]", reservationId);
        return reservationRepository.findById(reservationId)
                .map(reservationMapper::entityToDto)
                .orElseThrow(() -> {
                    throw new EntityNotFoundException("Reservation not found.");
                });
    }

    public ReservationDTO cancelReservation(Long reservationId) {
        return reservationMapper.entityToDto(this.cancel(reservationId));
    }

    private Reservation cancel(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservation -> {

            this.validateCancellation(reservation);

            BigDecimal refundValue = getRefundValue(reservation);
            return this.updateReservation(reservation, refundValue, ReservationStatus.CANCELLED);

        }).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    private Reservation updateReservation(Reservation reservation, BigDecimal refundValue, ReservationStatus status) {
        reservation.setReservationStatus(status);
        reservation.setValue(reservation.getValue().subtract(refundValue));
        reservation.setRefundValue(refundValue);

        return reservationRepository.save(reservation);
    }

    private void validateCancellation(Reservation reservation) {
        if (!ReservationStatus.READY_TO_PLAY.equals(reservation.getReservationStatus())) {
            throw new IllegalArgumentException("Cannot cancel/reschedule because it's not in ready to play status.");
        }

        if (reservation.getSchedule().getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Can cancel/reschedule only future dates.");
        }
    }

    public BigDecimal getRefundValue(Reservation reservation) {
//        As a Tennis Court Admin, I want to keep
//        25% of the reservation fee if the User cancels or reschedules between 12:00 and 23:59 hours in advance,
//        50% between 2:00 and 11:59 in advance, and
//        75% between 0:01 and 2:00 in advance
        long minutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), reservation.getSchedule().getStartDateTime());

        BigDecimal value = reservation.getValue();
        if (minutes >= 1440) {
            return value;
        } else if (minutes >= 720) {
            return new BigDecimal("0.25").multiply(value);
        } else if (minutes >= 120) {
            return new BigDecimal("0.50").multiply(value);
        } else if (minutes >= 1) {
            return new BigDecimal("0.75").multiply(value);
        }
        return BigDecimal.ZERO;
    }

    /*TODO: This method actually not fully working, find a way to fix the issue when it's throwing the error:
            "Cannot reschedule to the same slot.*/
    public ReservationDTO rescheduleReservation(Long previousReservationId, Long scheduleId) {
        Reservation previousReservation = cancel(previousReservationId);

        if (scheduleId.equals(previousReservation.getSchedule().getId())) {
            throw new IllegalArgumentException("Cannot reschedule to the same slot.");
        }

        previousReservation.setReservationStatus(ReservationStatus.RESCHEDULED);
        reservationRepository.save(previousReservation);

        ReservationDTO newReservation = bookReservation(CreateReservationRequestDTO.builder()
                .guestId(previousReservation.getGuest().getId())
                .scheduleId(scheduleId)
                .build());
        newReservation.setPreviousReservation(reservationMapper.entityToDto(previousReservation));
        return newReservation;
    }
}
