package org.bangbanghub.deliverypersonservice.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface DeliveryPersonRepository extends JpaRepository<DeliveryPerson, UserId>,
    JpaSpecificationExecutor<DeliveryPerson> {

    // 마지막 배송 순번을 가진 담당자 조회 (순번 생성용)
    /**
     * Retrieves the DeliveryPerson with the largest deliveryOrder.value.
     *
     * @return an Optional containing the DeliveryPerson that has the highest deliveryOrder.value, or an empty Optional if none exist
     */
    @Query("SELECT dp FROM DeliveryPerson dp ORDER BY dp.deliveryOrder.value DESC LIMIT 1")
    Optional<DeliveryPerson> findTopByOrderByDeliveryOrderDesc();
}