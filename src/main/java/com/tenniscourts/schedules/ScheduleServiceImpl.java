package com.tenniscourts.schedules;

import com.tenniscourts.exceptions.AlreadyExistsEntityException;
import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.tenniscourts.TennisCourt;
import com.tenniscourts.tenniscourts.TennisCourtRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final TennisCourtRepository tennisCourtRepository;
    private final ScheduleMapper scheduleMapper;


    @Override
    public ScheduleDTO addSchedule(CreateScheduleRequestDTO createScheduleRequestDTO) {
        log.debug("[SERVICE] Create schedule for tennis court [{}]", createScheduleRequestDTO.getTennisCourtId());
        TennisCourt tennisCourt = tennisCourtRepository.findById(createScheduleRequestDTO.getTennisCourtId())
                .orElseThrow(() -> {
                    throw new EntityNotFoundException("Can't schedule on non existing entity");
                });
        LocalDateTime startOfHour = createScheduleRequestDTO.getStartDateTime().withMinute(0);
        checkOverlappingSchedule(startOfHour, tennisCourt);
        Schedule transientSchedule = Schedule.builder()
                .startDateTime(startOfHour)
                .endDateTime(startOfHour.plusHours(1))
                .tennisCourt(tennisCourt)
                .build();
        return scheduleMapper.entityToDto(scheduleRepository.save(transientSchedule));
    }

    private void checkOverlappingSchedule(LocalDateTime time, TennisCourt tennisCourt) {
        //also we can check a date range as enhancement
        log.trace("[SERVICE] Check for overlapping schedules for [{}] starting on [{}]", tennisCourt.getName(), time);
        if (scheduleRepository.countByTennisCourtAndStartDateTime(tennisCourt, time) > 0) {
            throw new AlreadyExistsEntityException("Overlapping schedule for this tennis court");
        }
    }

    @Override
    public List<ScheduleDTO> findSchedulesByDates(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("[SERVICE] Find schedules for date range [{} - {}]", startDate, endDate);
        return scheduleMapper.entityToDto(scheduleRepository.findAllByStartDateTimeGreaterThanAndEndDateTimeLessThan(startDate, endDate));
    }

    @Override
    public ScheduleDTO findSchedule(Long scheduleId) {
        log.debug("[SERVICE] Find schedule by id: [{}]", scheduleId);
        return scheduleRepository.findById(scheduleId)
                .map(scheduleMapper::entityToDto)
                .orElseThrow(() -> {
                    throw new EntityNotFoundException(String.format("Schedule [%d] not found", scheduleId));
                });
    }

    @Override
    public List<ScheduleDTO> findSchedulesByTennisCourtId(Long tennisCourtId) {
        log.debug("[SERVICE] Find schedules for tennis court [{}]", tennisCourtId);
        return scheduleMapper.entityToDto(scheduleRepository.findByTennisCourt_IdOrderByStartDateTime(tennisCourtId));
    }
}
