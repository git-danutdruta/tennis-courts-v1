package com.tenniscourts.guests;


import com.tenniscourts.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class GuestServiceImpl implements GuestService {
    private final GuestMapper guestMapper;
    private final GuestRepository guestRepository;

    @Override
    public GuestDTO createGuest(GuestDTO guestDto) {
        log.debug("[SERVICE] Create new guest");
        Guest transientGuest = guestRepository.save(guestMapper.dtoToNewEntity(guestDto));
        return guestMapper.entityToDto(transientGuest);
    }

    @Override
    public GuestDTO updateGuest(GuestDTO guestDto) {
        Long guestId = guestDto.getId();
        log.debug("[SERVICE] Update guest [{}]", guestId);
        Guest currentGuest = guestRepository.findById(guestId).orElseThrow(() -> new EntityNotFoundException(String.format("Guest [%d] not found", guestId)));
        currentGuest.setName(guestDto.getName());
        return guestMapper.entityToDto(guestRepository.save(currentGuest));
    }

    @Override
    public void deleteGuest(Long guestId) {
        log.debug("[SERVICE] Delete guest by id: [{}]", guestId);
        Guest existingGuest = guestRepository.findById(guestId).orElseThrow(() -> new EntityNotFoundException("Guest not found"));
        guestRepository.delete(existingGuest);
    }

    @Override
    public List<GuestDTO> findAllGuest() {
        log.debug("[SERVICE] Find all guests");
        return guestMapper.entityToDto(guestRepository.findAll());
    }

    @Override
    public GuestDTO findGuestById(Long guestId) {
        log.debug("[SERVICE] Find guest by Id [{}]", guestId);
        return guestRepository.findById(guestId).
                map(guestMapper::entityToDto)
                .orElseThrow(() -> new EntityNotFoundException("Guest not found"));
    }

    @Override
    public List<GuestDTO> findGuestByName(String name) {
        log.debug("[SERVICE] Find guest/s by name [{}]", name);
        return guestMapper.entityToDto(guestRepository.findAllByName(name));
    }
}