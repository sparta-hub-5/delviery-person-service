package org.bangbanghub.deliverypersonservice.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.bangbanghub.deliverypersonservice.application.dto.DeliveryPersonDto;
import org.bangbanghub.deliverypersonservice.application.service.DeliveryPersonService;
import org.bangbanghub.deliverypersonservice.domain.DeliveryPersonStatus;
import org.bangbanghub.deliverypersonservice.domain.DeliveryPersonType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/delivery-persons")
@RequiredArgsConstructor
public class DeliveryPersonController {

    private final DeliveryPersonService deliveryPersonService;

    @PostMapping
    public ResponseEntity<UUID> create(@RequestBody @jakarta.validation.Valid DeliveryPersonDto.CreateRequest request) {
        UUID createdId = deliveryPersonService.create(request);
        return ResponseEntity.created(URI.create("/api/delivery-persons/" + createdId)).body(createdId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<DeliveryPersonDto.Response> get(@PathVariable UUID userId) {
        return ResponseEntity.ok(deliveryPersonService.getDeliveryPerson(userId));
    }

    @GetMapping
    public ResponseEntity<Page<DeliveryPersonDto.Response>> search(
            @RequestParam(required = false) UUID hubId,
            @RequestParam(required = false) DeliveryPersonType type,
            @RequestParam(required = false) DeliveryPersonStatus status,
            @RequestParam(required = false) String name,
            @PageableDefault(sort = "deliveryOrder.value", direction = Sort.Direction.ASC) Pageable pageable) {
        var condition = new DeliveryPersonDto.SearchCondition(hubId, type, status, name);
        return ResponseEntity.ok(deliveryPersonService.search(condition, pageable));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> update(
            @PathVariable UUID userId,
            @RequestBody @jakarta.validation.Valid DeliveryPersonDto.UpdateRequest request) {
        deliveryPersonService.update(userId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable UUID userId) {
        deliveryPersonService.delete(userId);
        return ResponseEntity.ok().build();
    }
}