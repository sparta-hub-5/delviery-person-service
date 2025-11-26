package org.bangbanghub.deliverypersonservice.application.service;

import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.bangbanghub.deliverypersonservice.application.dto.DeliveryPersonDto;
import org.bangbanghub.deliverypersonservice.application.dto.DeliveryPersonDto.Response;
import org.bangbanghub.deliverypersonservice.domain.DeliveryOrder;
import org.bangbanghub.deliverypersonservice.domain.DeliveryPerson;
import org.bangbanghub.deliverypersonservice.domain.DeliveryPersonRepository;
import org.bangbanghub.deliverypersonservice.domain.HubId;
import org.bangbanghub.deliverypersonservice.domain.HubValidator;
import org.bangbanghub.deliverypersonservice.domain.SlackId;
import org.bangbanghub.deliverypersonservice.domain.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryPersonService {

    private final DeliveryPersonRepository deliveryPersonRepository;
    private final HubValidator hubValidator; // 도메인 서비스(Validator) 주입

    // [Create]
    @Transactional
    public UUID create(DeliveryPersonDto.CreateRequest request) {
        UserId userId = new UserId(request.userId());

        // 1. 중복 검사 (이미 등록된 사용자인지)
        if (deliveryPersonRepository.existsById(userId)) {
            throw new IllegalArgumentException("이미 등록된 배송 담당자입니다.");
        }

        // 2. 마지막 순번 조회
        DeliveryOrder lastOrder = deliveryPersonRepository.findTopByOrderByDeliveryOrderDesc()
            .map(DeliveryPerson::getDeliveryOrder)
            .orElse(null);

        // 3. 엔티티 생성 (도메인 로직: 순번 계산, 허브 검증 등 수행)
        DeliveryPerson deliveryPerson = DeliveryPerson.create(
            userId,
            request.hubId() != null ? new HubId(request.hubId()) : null,
            SlackId.of(request.slackId()),
            request.type(),
            request.name(),
            request.phoneNumber(),
            lastOrder,
            hubValidator
        );

        deliveryPersonRepository.save(deliveryPerson);
        return userId.getId();
    }

    // [Read - Single]
    public DeliveryPersonDto.Response getDeliveryPerson(UUID userId) {
        DeliveryPerson deliveryPerson = findByIdOrThrow(new UserId(userId));
        return DeliveryPersonDto.Response.from(deliveryPerson);
    }

    // [Read - Search & Pagination]
    public Page<Response> search(DeliveryPersonDto.SearchCondition condition, Pageable pageable) {
        Specification<DeliveryPerson> spec = (root, query, cb) -> cb.conjunction();

        if (condition.hubId() != null) {
            spec = spec.and((root, query, cb) ->
                cb.equal(root.get("hubId").get("id"), condition.hubId()));
        }
        if (condition.type() != null) {
            spec = spec.and((root, query, cb) ->
                cb.equal(root.get("type"), condition.type()));
        }
        if (condition.status() != null) {
            spec = spec.and((root, query, cb) ->
                cb.equal(root.get("status"), condition.status()));
        }
        if (condition.name() != null && !condition.name().isBlank()) {
            spec = spec.and((root, query, cb) ->
                cb.like(root.get("name"), "%" + condition.name() + "%"));
        }

        return deliveryPersonRepository.findAll(spec, pageable)
            .map(DeliveryPersonDto.Response::from);
    }

    // [Update]
    @Transactional
    public void update(UUID userId, DeliveryPersonDto.UpdateRequest request) {
        DeliveryPerson deliveryPerson = findByIdOrThrow(new UserId(userId));

        // 도메인 메서드를 통해 변경 (허브 필수 여부 등 재검증)
        deliveryPerson.update(
            request.hubId() != null ? new HubId(request.hubId()) : null,
            SlackId.of(request.slackId()),
            request.type(),
            request.name(),
            request.phoneNumber(),
            hubValidator
        );
    }

    // [Delete]
    @Transactional
    public void delete(UUID userId) {
        DeliveryPerson deliveryPerson = findByIdOrThrow(new UserId(userId));
        deliveryPerson.delete(); // Soft Delete
    }

    private DeliveryPerson findByIdOrThrow(UserId userId) {
        return deliveryPersonRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("배송 담당자를 찾을 수 없습니다."));
    }
}