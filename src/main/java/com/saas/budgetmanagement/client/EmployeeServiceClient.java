package com.saas.budgetmanagement.client;

import com.saas.budgetmanagement.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "employee-service", path = "/api/employee")
public interface EmployeeServiceClient {

    @GetMapping("/employees/{tenant-id}/get/{employee-id}")
    EmployeeDto getEmployeeById(@PathVariable("tenant-id") UUID tenantId,
                                @PathVariable("employee-id") UUID employeeId);

}
