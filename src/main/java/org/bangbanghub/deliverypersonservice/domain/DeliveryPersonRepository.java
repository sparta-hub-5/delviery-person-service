package org.bangbanghub.deliverypersonservice.domain;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface DeliveryPersonRepository extends JpaRepository<DeliveryPerson, UserId>,
        JpaSpecificationExecutor<DeliveryPerson> {

    @Query("SELECT dp FROM DeliveryPerson dp ORDER BY dp.deliveryOrder.value DESC LIMIT 1")
    Optional<DeliveryPerson> findTopByOrderByDeliveryOrderDesc();

    @Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT dp FROM DeliveryPerson dp ORDER BY dp.deliveryOrder.value DESC LIMIT 1")
    Optional<DeliveryPerson> findTopByOrderByDeliveryOrderDescWithLock();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT dp FROM DeliveryPerson dp ORDER BY dp.deliveryOrder.value DESC")
    Optional<DeliveryPerson> findTopByOrderByDeliveryOrderDescForUpdate();
}


