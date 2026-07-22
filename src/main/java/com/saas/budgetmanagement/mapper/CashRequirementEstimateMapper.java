package com.saas.budgetmanagement.mapper;

import com.saas.budgetmanagement.dto.responseDto.CashRequirementEstimateResponse;
import com.saas.budgetmanagement.model.CashRequirementEstimate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CashRequirementEstimateMapper {

    @Mapping(target = "advisoryNote", ignore = true)
    CashRequirementEstimateResponse toResponse(CashRequirementEstimate entity);
}