package com.saas.budgetmanagement.mapper;

import com.saas.budgetmanagement.dto.responseDto.BudgetLedgerResponse;
import com.saas.budgetmanagement.model.BudgetLedger;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BudgetLedgerMapper {

    @Mapping(target = "accountName", ignore = true)
    @Mapping(target = "costCenterName", ignore = true)
    @Mapping(target = "departmentName", ignore = true)
    @Mapping(target = "fundSourceName", ignore = true)
    @Mapping(target = "projectName", ignore = true)
    BudgetLedgerResponse toResponse(BudgetLedger entity);
}