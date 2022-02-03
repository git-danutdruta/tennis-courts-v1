package com.tenniscourts.tenniscourts;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.schedules.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TennisCourtServiceImpl implements TennisCourtService {

    private final TennisCourtRepository tennisCourtRepository;
    private final TennisCourtMapper tennisCourtMapper;
    private final ScheduleService scheduleService;

    @Override
    public TennisCourtDTO addTennisCourt(TennisCourtDTO tennisCourt) {
        log.debug("[SERVICE] Add tennis court [{}]", tennisCourt.getName());
        TennisCourt savedEntity = tennisCourtRepository.save(tennisCourtMapper.dtoToEntity(tennisCourt));
        return tennisCourtMapper.entityToDto(savedEntity);
    }

    @Override
    public TennisCourtDTO findTennisCourtById(Long id) {
        log.debug("[SERVICE] Find tennis court by id [{}]", id);
        return tennisCourtRepository.findById(id)
                .map(tennisCourtMapper::entityToDto)
                .orElseThrow(() -> {
                    throw new EntityNotFoundException("Tennis Court not found.");
                });
    }

    @Override
    public TennisCourtDTO findTennisCourtWithSchedulesById(Long tennisCourtId) {
        log.debug("[SERVICE] Find tennis court with schedules calendar filled");
        TennisCourtDTO tennisCourtDTO = findTennisCourtById(tennisCourtId);
        tennisCourtDTO.setTennisCourtSchedules(scheduleService.findSchedulesByTennisCourtId(tennisCourtId));
        return tennisCourtDTO;
    }
}
