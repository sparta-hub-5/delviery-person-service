package org.bangbanghub.deliverypersonservice.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bangbang.common.jpa.BaseEntity;

@Getter
@Entity
@ToString
@Access(AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_delivery_person")
public class DeliveryPerson extends BaseEntity {

    @EmbeddedId
    private UserId id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "hub_id", nullable = true, columnDefinition = "UUID"))
    private HubId hubId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "slack_id", nullable = false))
    private SlackId slackId;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryPersonType type;

    @Embedded
    private DeliveryOrder deliveryOrder;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private DeliveryPersonStatus status;

    private DeliveryPerson(UserId id, HubId hubId, SlackId slackId,
        DeliveryPersonType type, DeliveryOrder deliveryOrder,
        String name, String phoneNumber, DeliveryPersonStatus status) {
        this.id = id;
        this.hubId = hubId;
        this.slackId = slackId;
        this.type = type;
        this.deliveryOrder = deliveryOrder;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }
    public static DeliveryPerson create(
        UserId userId,
        HubId hubId,
        SlackId slackId,
        DeliveryPersonType type,
        String name,
        String phoneNumber,
        DeliveryOrder lastOrder,
        HubValidator hubValidator
    ) {
        validateHubRequirement(type, hubId, hubValidator);

        DeliveryOrder nextOrder = (lastOrder == null) ? DeliveryOrder.first() : lastOrder.next();

        return new DeliveryPerson(
            userId,
            hubId,
            slackId,
            type,
            nextOrder,
            name,
            phoneNumber,
            DeliveryPersonStatus.ACTIVE
        );
    }

    public void update(
        HubId hubId,
        SlackId slackId,
        DeliveryPersonType type,
        String name,
        String phoneNumber,
        HubValidator hubValidator
    ) {
        validateHubRequirement(type, hubId, hubValidator);

        this.hubId = hubId;
        this.slackId = slackId;
        this.type = type;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public void delete() {
        this.status = DeliveryPersonStatus.INACTIVE;
    }

    private static void validateHubRequirement(DeliveryPersonType type, HubId hubId, HubValidator hubValidator) {
        if (type == DeliveryPersonType.COMPANY_DELIVERY_PERSON) {
            if (hubId == null) {
                throw new IllegalArgumentException("업체 배송 담당자는 허브 ID가 필수입니다.");
            }
            if (!hubValidator.exists(hubId)) {
                throw new IllegalArgumentException("존재하지 않는 허브입니다.");
            }
        }
    }
}