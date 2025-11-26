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

    /**
     * Private constructor that initializes a DeliveryPerson with the given identity and domain attributes.
     *
     * @param id the user's embedded identifier
     * @param hubId the embedded hub identifier (may be null for delivery person types that do not require a hub)
     * @param slackId the embedded Slack identifier
     * @param type the delivery person type
     * @param deliveryOrder the delivery person's ordering information within a hub's sequence
     * @param name the delivery person's display name (max length 50)
     * @param phoneNumber the delivery person's contact phone number (max length 20)
     * @param status the delivery person's current status
     */
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

    /**
     * Create a new DeliveryPerson with a computed delivery order and initial ACTIVE status.
     *
     * The new delivery order is the next sequence after {@code lastOrder} (or the first order if {@code lastOrder} is {@code null}). For COMPANY_DELIVERY_PERSON, a non-null and existing {@code hubId} is required.
     *
     * @param userId the identifier for the delivery person
     * @param hubId the hub identifier (may be null unless {@code type} is COMPANY_DELIVERY_PERSON)
     * @param slackId the Slack identifier for the delivery person
     * @param type the delivery person type
     * @param name the delivery person's name (max length 50)
     * @param phoneNumber the delivery person's phone number (max length 20)
     * @param lastOrder the current last delivery order in the sequence, or {@code null} if none
     * @return the newly created DeliveryPerson with status set to {@code ACTIVE} and a computed delivery order
     */
    public static DeliveryPerson create(
        UserId userId,
        HubId hubId,
        SlackId slackId,
        DeliveryPersonType type,
        String name,
        String phoneNumber,
        DeliveryOrder lastOrder, // 현재 가장 마지막 순번 (없으면 null)
        HubValidator hubValidator // 허브 검증 로직 주입
    ) {
        // 도메인 유효성 검증
        validateHubRequirement(type, hubId, hubValidator);

        // 순번 계산: 마지막 순번이 없으면 1, 있으면 다음 번호
        DeliveryOrder nextOrder = (lastOrder == null) ? DeliveryOrder.first() : lastOrder.next();

        return new DeliveryPerson(
            userId,
            hubId,
            slackId,
            type,
            nextOrder,
            name,
            phoneNumber,
            DeliveryPersonStatus.ACTIVE // 초기 상태는 ACTIVE
        );
    }

    /**
     * Update the delivery person's hub, Slack ID, type, name, and phone number while enforcing hub-related rules.
     *
     * @param hubId        the new hub identifier (may be null unless required by the delivery person type)
     * @param slackId      the new Slack identifier
     * @param type         the new delivery person type
     * @param name         the new display name (max length constraints apply elsewhere)
     * @param phoneNumber  the new phone number
     * @param hubValidator validator used to verify hub existence when required
     * @throws IllegalArgumentException if the delivery person type requires a hub but none is provided or if the hub does not exist
     */
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

    /**
     * Marks the delivery person as inactive to perform a soft delete.
     *
     * Sets the entity's status to DeliveryPersonStatus.INACTIVE without reordering delivery sequence.
     */
    public void delete() {
        this.status = DeliveryPersonStatus.INACTIVE; // 또는 SUSPENDED, 정책에 따라 결정
    }

    /**
     * Ensures that a hub is provided and exists when the delivery person type is COMPANY_DELIVERY_PERSON.
     *
     * @param type         the delivery person type to check
     * @param hubId        the hub identifier that must be present for company delivery persons
     * @param hubValidator validator used to check hub existence
     * @throws IllegalArgumentException if the type is COMPANY_DELIVERY_PERSON and either `hubId` is null or the hub does not exist
     */
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