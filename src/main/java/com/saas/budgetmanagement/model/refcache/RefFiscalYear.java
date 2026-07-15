package com.saas.budgetmanagement.model.refcache;

import com.saas.budgetmanagement.model.Base;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "ref_fiscal_years")
@Data
@EqualsAndHashCode(callSuper = true)
public class RefFiscalYear extends Base {
}