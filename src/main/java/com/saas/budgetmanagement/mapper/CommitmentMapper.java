package com.saas.budgetmanagement.mapper;

import com.saas.budgetmanagement.dto.eventDto.outbound.CommitmentRegisteredEvent;
import com.saas.budgetmanagement.dto.eventDto.outbound.CommitmentLiquidatedEvent;
import com.saas.budgetmanagement.dto.responseDto.CommitmentResponse;
import com.saas.budgetmanagement.model.Commitment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommitmentMapper {

    @Mapping(target = "accountName", ignore = true)
    @Mapping(target = "costCenterName", ignore = true)
    @Mapping(target = "departmentName", ignore = true)
    @Mapping(target = "fundSourceName", ignore = true)
    @Mapping(target = "projectName", ignore = true)
    CommitmentResponse toResponse(Commitment entity);

    CommitmentRegisteredEvent toRegisteredEvent(Commitment entity);

    CommitmentLiquidatedEvent toLiquidatedEvent(Commitment entity);
}