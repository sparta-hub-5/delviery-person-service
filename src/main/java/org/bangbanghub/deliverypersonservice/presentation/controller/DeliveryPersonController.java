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
@RequestMapping("/api/delivery-persons")
@RequiredArgsConstructor
public class DeliveryPersonController {

    private final DeliveryPersonService deliveryPersonService;

    /**
     * Create a new delivery person.
     *
     * Creates a delivery person from the provided request and responds with HTTP 201 Created.
     * The response includes a Location header pointing to the new resource (/api/delivery-persons/{id})
     * and the created delivery person's UUID in the response body.
     *
     * @param request the DTO containing data required to create a delivery person
     * @return the UUID of the created delivery person
     */
    @PostMapping
    public ResponseEntity<UUID> create(@RequestBody DeliveryPersonDto.CreateRequest request) {
        UUID createdId = deliveryPersonService.create(request);
        return ResponseEntity.created(URI.create("/api/delivery-persons/" + createdId)).body(createdId);
    }

    /**
     * Retrieve a delivery person by their user ID.
     *
     * @param userId the UUID of the delivery person to fetch
     * @return a ResponseEntity containing the delivery person's details as a DeliveryPersonDto.Response
     */
    @GetMapping("/{userId}")
    public ResponseEntity<DeliveryPersonDto.Response> get(@PathVariable UUID userId) {
        return ResponseEntity.ok(deliveryPersonService.getDeliveryPerson(userId));
    }

    // 검색 및 목록 조회 (페이징)
    /**
     * Searches delivery persons using optional filter criteria and returns a paginated result.
     *
     * @param hubId   optional hub UUID to filter delivery persons by assigned hub
     * @param type    optional delivery person type to filter results
     * @param status  optional delivery person status to filter results
     * @param name    optional name filter
     * @param pageable pagination and sorting information (default sort: `deliveryOrder.value` ascending)
     * @return a page of DeliveryPersonDto.Response matching the provided filters
     */
    @GetMapping
    public ResponseEntity<Page<DeliveryPersonDto.Response>> search(
        @RequestParam(required = false) UUID hubId,
        @RequestParam(required = false) DeliveryPersonType type,
        @RequestParam(required = false) DeliveryPersonStatus status,
        @RequestParam(required = false) String name,
        @PageableDefault(sort = "deliveryOrder.value", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        var condition = new DeliveryPersonDto.SearchCondition(hubId, type, status, name);
        return ResponseEntity.ok(deliveryPersonService.search(condition, pageable));
    }

    /**
     * Update an existing delivery person's details.
     *
     * @param userId  the UUID of the delivery person to update
     * @param request the update payload containing the delivery person's new values
     * @return        a ResponseEntity with HTTP 200 OK and no body
     */
    @PutMapping("/{userId}")
    public ResponseEntity<Void> update(
        @PathVariable UUID userId,
        @RequestBody DeliveryPersonDto.UpdateRequest request
    ) {
        deliveryPersonService.update(userId, request);
        return ResponseEntity.ok().build();
    }

    /**
     * Perform a soft delete of a delivery person identified by the given user ID.
     *
     * @param userId the UUID of the delivery person to soft-delete
     * @return a ResponseEntity with HTTP 200 OK and no body
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable UUID userId) {
        deliveryPersonService.delete(userId);
        return ResponseEntity.ok().build();
    }
}