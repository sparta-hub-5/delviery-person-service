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

    // 생성자: 팩토리 메서드를 통해서만 호출됨
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
     * 배송 담당자 생성 (Factory Method)
     * - 규칙 1: 새로운 담당자는 마지막 순번 + 1로 설정됨
     * - 규칙 2: 업체 배송 담당자는 유효한 허브 ID가 필수
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
     * 정보 수정
     * - 규칙: 타입이 변경되거나 허브가 변경될 때도 업체 담당자 규칙 검증 필요
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
     * 삭제 (Soft Delete)
     * - 규칙: 삭제 시 배송 순번을 재배열하지 않음 (상태만 변경)
     */
    public void delete() {
        this.status = DeliveryPersonStatus.INACTIVE; // 또는 SUSPENDED, 정책에 따라 결정
    }

    // 내부 검증 로직: 업체 배송 담당자인 경우 허브 ID 존재 및 유효성 확인
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