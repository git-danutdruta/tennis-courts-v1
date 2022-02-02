package com.tenniscourts.reservations;

import com.tenniscourts.common.CommonMapper;
import com.tenniscourts.common.config.CommonMapperConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CommonMapperConfiguration.class)
public interface ReservationMapper extends CommonMapper<ReservationDTO, Reservation> {

    @Mapping(target = "guest.id", source = "guestId")
    @Mapping(target = "schedule.id", source = "scheduleId")
    Reservation map(CreateReservationRequestDTO source);
}
