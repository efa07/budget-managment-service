package com.saas.budgetmanagement.mapper;

import com.saas.budgetmanagement.dto.eventDto.outbound.BudgetTransferApprovedEvent;
import com.saas.budgetmanagement.dto.eventDto.outbound.BudgetTransferRequestedEvent;
import com.saas.budgetmanagement.dto.response.BudgetTransferResponse;
import com.saas.budgetmanagement.model.BudgetTransfer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BudgetTransferMapper {

    BudgetTransferResponse toResponse(BudgetTransfer entity);

    BudgetTransferRequestedEvent toRequestedEvent(BudgetTransfer entity);

    BudgetTransferApprovedEvent toApprovedEvent(BudgetTransfer entity);
}