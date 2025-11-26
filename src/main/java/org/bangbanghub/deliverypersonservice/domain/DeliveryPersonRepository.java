package org.bangbanghub.deliverypersonservice.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface DeliveryPersonRepository extends JpaRepository<DeliveryPerson, UserId>,
    JpaSpecificationExecutor<DeliveryPerson> {

    // 마지막 배송 순번을 가진 담당자 조회 (순번 생성용)
    // DeliveryOrder는 @Embedded 타입이므로 내부 필드명(value)으로 접근
    @Query("SELECT dp FROM DeliveryPerson dp ORDER BY dp.deliveryOrder.value DESC LIMIT 1")
    Optional<DeliveryPerson> findTopByOrderByDeliveryOrderDesc();
}