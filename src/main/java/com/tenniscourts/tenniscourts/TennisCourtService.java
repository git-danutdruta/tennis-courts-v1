package com.tenniscourts.tenniscourts;

public interface TennisCourtService {
    /**
     * Create new tennis court.
     *
     * @param tennisCourt
     * @return TennisCourtDTO
     */
    TennisCourtDTO addTennisCourt(TennisCourtDTO tennisCourt);

    /**
     * Find tennis court by id.
     *
     * @param id tennis court identifier
     * @return TennisCourtDTO
     */
    TennisCourtDTO findTennisCourtById(Long id);

    /**
     * Find tennis court with schedules by id.
     *
     * @param tennisCourtId
     * @return TennisCourtDTO
     */
    TennisCourtDTO findTennisCourtWithSchedulesById(Long tennisCourtId);
}
