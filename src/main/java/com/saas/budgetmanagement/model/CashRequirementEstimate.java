package com.saas.budgetmanagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "cash_requirement_estimates")
@Data
@EqualsAndHashCode(callSuper = true)
public class CashRequirementEstimate extends Base {
}