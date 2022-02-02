package com.tenniscourts.schedules;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleService {
    /**
     * Add schedule.
     *
     * @param createScheduleRequestDTO
     * @return ScheduleDTO
     */
    ScheduleDTO addSchedule(CreateScheduleRequestDTO createScheduleRequestDTO);

    /**
     * Find schedules by date range.
     *
     * @param startDate starting point
     * @param endDate   stop point
     * @return List&lt;ScheduleDTO&gt;
     */
    List<ScheduleDTO> findSchedulesByDates(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find schedule by id.
     *
     * @param scheduleId
     * @return ScheduleDTO
     */
    ScheduleDTO findSchedule(Long scheduleId);

    /**
     * Find schedule by tennis court id.
     *
     * @param tennisCourtId
     * @return List&lt;ScheduleDTO&gt;
     */
    List<ScheduleDTO> findSchedulesByTennisCourtId(Long tennisCourtId);
}
