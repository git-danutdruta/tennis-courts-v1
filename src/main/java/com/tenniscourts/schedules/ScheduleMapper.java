package com.tenniscourts.schedules;

import com.tenniscourts.common.CommonMapper;
import com.tenniscourts.common.config.CommonMapperConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CommonMapperConfiguration.class)
public interface ScheduleMapper extends CommonMapper<ScheduleDTO, Schedule> {

    @Override
    @Mapping(source = "tennisCourt.id", target = "tennisCourtId")
    ScheduleDTO entityToDto(Schedule entity);
}
