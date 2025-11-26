package org.bangbanghub.deliverypersonservice.application.dto;

import lombok.Builder;
import org.bangbanghub.deliverypersonservice.domain.DeliveryPerson;
import org.bangbanghub.deliverypersonservice.domain.DeliveryPersonStatus;
import org.bangbanghub.deliverypersonservice.domain.DeliveryPersonType;

import java.util.UUID;

public record DeliveryPersonDto() {

    // [Create Request]
    public record CreateRequest(
        UUID userId,
        UUID hubId,
        String slackId,
        DeliveryPersonType type,
        String name,
        String phoneNumber
    ) {}

    // [Update Request]
    public record UpdateRequest(
        UUID hubId,
        String slackId,
        DeliveryPersonType type,
        String name,
        String phoneNumber
    ) {}

    // [Search Condition] 검색 조건
    public record SearchCondition(
        UUID hubId,
        DeliveryPersonType type,
        DeliveryPersonStatus status,
        String name
    ) {}

    // [Response]
    @Builder
    public record Response(
        UUID userId,
        UUID hubId,
        String slackId,
        DeliveryPersonType type,
        Long deliveryOrder,
        String name,
        String phoneNumber,
        DeliveryPersonStatus status
    ) {
        public static Response from(DeliveryPerson entity) {
            return Response.builder()
                .userId(entity.getId().getId())
                .hubId(entity.getHubId() != null ? entity.getHubId().getId() : null)
                .slackId(entity.getSlackId().getId())
                .type(entity.getType())
                .deliveryOrder(entity.getDeliveryOrder().getValue())
                .name(entity.getName())
                .phoneNumber(entity.getPhoneNumber())
                .status(entity.getStatus())
                .build();
        }
    }
}