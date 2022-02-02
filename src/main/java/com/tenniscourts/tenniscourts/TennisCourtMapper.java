package com.tenniscourts.tenniscourts;

import com.tenniscourts.common.CommonMapper;
import com.tenniscourts.common.config.CommonMapperConfiguration;
import org.mapstruct.Mapper;

@Mapper(config = CommonMapperConfiguration.class)
public interface TennisCourtMapper extends CommonMapper<TennisCourtDTO, TennisCourt> {
}
