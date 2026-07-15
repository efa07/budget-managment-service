package com.saas.budgetmanagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "budget_revisions")
@Data
@EqualsAndHashCode(callSuper = true)
public class BudgetRevision extends Base {
}