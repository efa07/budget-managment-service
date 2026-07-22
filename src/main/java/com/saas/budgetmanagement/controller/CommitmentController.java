package com.saas.budgetmanagement.controller;

import com.saas.budgetmanagement.dto.requestDto.AdjustCommitmentRequest;
import com.saas.budgetmanagement.dto.responseDto.CommitmentResponse;
import com.saas.budgetmanagement.enums.SourceService;
import com.saas.budgetmanagement.service.CommitmentService;
import com.saas.budgetmanagement.utility.PermissionUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/budget-management/commitments/{tenantId}")
@RequiredArgsConstructor
@Tag(name = "Commitment")
public class CommitmentController {

    private final CommitmentService commitmentService;
    private final PermissionUtil permissionUtil;

    @GetMapping
    public ResponseEntity<Page<CommitmentResponse>> list(@PathVariable UUID tenantId,
                                                           @RequestParam SourceService sourceService,
                                                           @RequestParam(required = false) String status,
                                                           Pageable pageable) {
        return ResponseEntity.ok(commitmentService.list(sourceService, status, pageable));
    }

    @PostMapping("/{id}/adjust")
    public ResponseEntity<CommitmentResponse> adjust(@PathVariable UUID tenantId, @PathVariable UUID id,
                                                       @Valid @RequestBody AdjustCommitmentRequest request) {
        if (!permissionUtil.hasPermission(tenantId, "commitment-manual-adjust")) {
            throw new AccessDeniedException("You do not have permission to manually adjust commitments");
        }
        return ResponseEntity.ok(commitmentService.adjustManually(id, request.getAdjustmentAmount(), request.getReason()));
    }
}