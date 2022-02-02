package com.tenniscourts.guests;


import com.tenniscourts.config.BaseRestController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/guest")
@AllArgsConstructor
public class GuestController extends BaseRestController {
    private final GuestService guestService;

    @PostMapping
    public ResponseEntity<Void> createGuest(@RequestBody GuestDTO newGuestDto) {
        log.info("[REST] Create new guest [{}]", newGuestDto.getName());
        //also we can retrieve the whole object to prevent findBy ... calls
        return ResponseEntity.created(locationByEntity(guestService.createGuest(newGuestDto).getId())).build();
    }

    @PutMapping
    public ResponseEntity<GuestDTO> updateGuest(@RequestBody GuestDTO guestDTO) {
        log.info("[REST] Update guest [{}]", guestDTO.getId());
        return ResponseEntity.ok(guestService.updateGuest(guestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuest(@PathVariable("id") Long guestId) {
        log.info("[REST] Delete guest");
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<GuestDTO>> findAllGuest() {
        log.info("[REST] Find all guests");
        return ResponseEntity.ok(guestService.findAllGuest());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuestDTO> findGuestById(@PathVariable("id") Long guestId) {
        log.info("[REST] Find guest by Id [{}]", guestId);
        return ResponseEntity.ok(guestService.findGuestById(guestId));
    }

    @GetMapping(params = "name")
    public ResponseEntity<List<GuestDTO>> findGuestByName(@RequestParam("name") String name) {
        //if assume that name is unique then we should return only one GuestDto instance
        //but if name is not UNIQUE constrained, then we should retrieve a list of guests.
        log.info("[REST] Find guest by name: [{}]", name);
        return ResponseEntity.ok(guestService.findGuestByName(name));
    }
}