package org.bangbanghub.deliverypersonservice.infrastructure;

import org.bangbanghub.deliverypersonservice.domain.HubId;
import org.bangbanghub.deliverypersonservice.domain.HubValidator;
import org.springframework.stereotype.Component;

@Component
public class HubValidatorImpl implements HubValidator {

    /**
     * Determines whether the hub identified by the given HubId exists.
     *
     * Currently this implementation returns {@code false} when {@code hubId} is {@code null}
     * and {@code true} for any non-null {@code hubId} as a placeholder until a Hub Service client
     * is integrated to perform a real existence check.
     *
     * @param hubId the hub identifier to check; may be {@code null}
     * @return {@code true} if the hub exists, {@code false} otherwise
     */

    @Override
    public boolean exists(HubId hubId) {
        if (hubId == null) return false;
        // 실제 구현: return hubClient.checkHubExists(hubId.getId());
        return true; // 테스트를 위해 무조건 true 반환
    }
}