package com.saas.budgetmanagement.mapper;

import com.saas.budgetmanagement.dto.eventDto.outbound.BudgetReturnApprovedEvent;
import com.saas.budgetmanagement.dto.eventDto.outbound.BudgetReturnRequestedEvent;
import com.saas.budgetmanagement.dto.responseDto.BudgetReturnResponse;
import com.saas.budgetmanagement.model.BudgetReturn;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BudgetReturnMapper {

    BudgetReturnResponse toResponse(BudgetReturn entity);

    BudgetReturnRequestedEvent toRequestedEvent(BudgetReturn entity);

    BudgetReturnApprovedEvent toApprovedEvent(BudgetReturn entity);
}