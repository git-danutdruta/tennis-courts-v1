package com.tenniscourts.schedules;

import com.tenniscourts.common.CommonMapper;
import com.tenniscourts.common.config.CommonMapperConfiguration;
import org.mapstruct.Mapper;

@Mapper(config = CommonMapperConfiguration.class)
public interface ScheduleMapper extends CommonMapper<ScheduleDTO, Schedule> {
}
