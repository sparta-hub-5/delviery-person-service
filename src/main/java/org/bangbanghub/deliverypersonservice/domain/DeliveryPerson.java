package org.bangbanghub.deliverypersonservice.domain;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
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
    @AttributeOverride(name = "id", column = @Column(name = "hub_id", nullable = false, columnDefinition = "UUID"))
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

    @Builder
    protected DeliveryPerson(UserId id, HubId hubId, SlackId slackId,
        DeliveryPersonType deliveryPersonType, Integer deliveryOrder
    ) {
    }


}
