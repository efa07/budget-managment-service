package com.saas.budgetmanagement.mapper;

import com.saas.budgetmanagement.dto.eventDto.outbound.BudgetAllocationCreatedEvent;
import com.saas.budgetmanagement.dto.responseDto.BudgetAllocationResponse;
import com.saas.budgetmanagement.model.BudgetAllocation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BudgetAllocationMapper {

    @Mapping(target = "accountName", ignore = true)
    @Mapping(target = "costCenterName", ignore = true)
    @Mapping(target = "departmentName", ignore = true)
    @Mapping(target = "fundSourceName", ignore = true)
    @Mapping(target = "projectName", ignore = true)
    BudgetAllocationResponse toResponse(BudgetAllocation entity);

    BudgetAllocationCreatedEvent toCreatedEvent(BudgetAllocation entity);
}