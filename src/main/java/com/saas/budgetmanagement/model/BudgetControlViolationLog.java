package com.saas.budgetmanagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "budget_control_violation_logs")
@Data
@EqualsAndHashCode(callSuper = true)
public class BudgetControlViolationLog extends Base {
}