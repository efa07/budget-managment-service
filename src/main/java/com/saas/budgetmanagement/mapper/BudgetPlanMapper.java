package com.saas.budgetmanagement.mapper;

import com.saas.budgetmanagement.dto.eventDto.outbound.BudgetPlanApprovedEvent;
import com.saas.budgetmanagement.dto.eventDto.outbound.BudgetPlanCreatedEvent;
import com.saas.budgetmanagement.dto.eventDto.outbound.BudgetPlanRejectedEvent;
import com.saas.budgetmanagement.dto.requestDto.BudgetLineItemRequest;
import com.saas.budgetmanagement.dto.responseDto.BudgetLineResponse;
import com.saas.budgetmanagement.dto.responseDto.BudgetPlanResponse;
import com.saas.budgetmanagement.model.BudgetLine;
import com.saas.budgetmanagement.model.BudgetPlan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface BudgetPlanMapper {

    @Mapping(target = "budgetPlanId", source = "budgetPlanId")
    BudgetLine toBudgetLine(BudgetLineItemRequest request, UUID budgetPlanId);

    @Mapping(target = "fiscalYearName", ignore = true)
    @Mapping(target = "lines", ignore = true)
    BudgetPlanResponse toResponseBase(BudgetPlan entity);

    @Mapping(target = "accountName", ignore = true)
    @Mapping(target = "costCenterName", ignore = true)
    @Mapping(target = "departmentName", ignore = true)
    @Mapping(target = "fundSourceName", ignore = true)
    @Mapping(target = "projectName", ignore = true)
    BudgetLineResponse toLineResponse(BudgetLine entity);

    List<BudgetLineResponse> toLineResponses(List<BudgetLine> entities);

    default BudgetPlanResponse toResponse(BudgetPlan entity, List<BudgetLine> lines) {
        BudgetPlanResponse response = toResponseBase(entity);
        response.setLines(toLineResponses(lines));
        return response;
    }

    BudgetPlanCreatedEvent toCreatedEvent(BudgetPlan entity);

    BudgetPlanApprovedEvent toApprovedEvent(BudgetPlan entity);

    BudgetPlanRejectedEvent toRejectedEvent(BudgetPlan entity);
}
