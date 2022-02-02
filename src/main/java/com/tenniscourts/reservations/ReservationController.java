package com.tenniscourts.reservations;

import com.tenniscourts.config.BaseRestController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/reservation")
@AllArgsConstructor
public class ReservationController extends BaseRestController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Void> bookReservation(@RequestBody CreateReservationRequestDTO createReservationRequestDTO) {
        log.info("[REST] Book reservation schedule [{}] for guest [{}] ", createReservationRequestDTO.getScheduleId(), createReservationRequestDTO.getGuestId());
        return ResponseEntity.created(locationByEntity(reservationService.bookReservation(createReservationRequestDTO).getId())).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> findReservation(@PathVariable("id") Long reservationId) {
        log.info("[REST] Find reservation by id: [{}]", reservationId);
        return ResponseEntity.ok(reservationService.findReservation(reservationId));
    }

    @GetMapping(value = "/cancel", params = "id")
    public ResponseEntity<ReservationDTO> cancelReservation(@RequestParam("id") Long reservationId) {
        log.info("[REST] Cancel reservation with id: [{}]", reservationId);
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

    @GetMapping(value = "/reschedule", params = {"reservationId", "scheduleId"})
    public ResponseEntity<ReservationDTO> rescheduleReservation(@RequestParam("reservationId") Long reservationId, @RequestParam("scheduleId") Long scheduleId) {
        log.info("[REST Reschedule reservation [{}] on schedule [{}]", reservationId, scheduleId);
        return ResponseEntity.ok(reservationService.rescheduleReservation(reservationId, scheduleId));
    }
}
