package com.saas.budgetmanagement.mapper;

import com.saas.budgetmanagement.dto.eventDto.outbound.BudgetRevisionCreatedEvent;
import com.saas.budgetmanagement.dto.responseDto.BudgetRevisionResponse;
import com.saas.budgetmanagement.model.BudgetRevision;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BudgetRevisionMapper {

    BudgetRevisionResponse toResponse(BudgetRevision entity);

    BudgetRevisionCreatedEvent toCreatedEvent(BudgetRevision entity);
}