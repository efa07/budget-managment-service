package com.saas.budgetmanagement.mapper;

import com.saas.budgetmanagement.dto.responseDto.BudgetLedgerResponse;
import com.saas.budgetmanagement.model.BudgetLedger;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BudgetLedgerMapper {

    BudgetLedgerResponse toResponse(BudgetLedger entity);
}