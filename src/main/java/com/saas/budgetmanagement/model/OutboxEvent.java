package com.saas.budgetmanagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "outbox_events")
@Data
@EqualsAndHashCode(callSuper = true)
public class OutboxEvent extends Base {
}