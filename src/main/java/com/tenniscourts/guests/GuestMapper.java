package com.tenniscourts.guests;

import com.tenniscourts.common.CommonMapper;
import com.tenniscourts.common.config.CommonMapperConfiguration;
import org.mapstruct.Mapper;

@Mapper(config = CommonMapperConfiguration.class)
public interface GuestMapper extends CommonMapper<GuestDTO, Guest> {
}
