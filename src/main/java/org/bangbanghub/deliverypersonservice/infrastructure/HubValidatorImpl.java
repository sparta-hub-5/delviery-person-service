package org.bangbanghub.deliverypersonservice.infrastructure;

import org.bangbanghub.deliverypersonservice.domain.HubId;
import org.bangbanghub.deliverypersonservice.domain.HubValidator;
import org.springframework.stereotype.Component;

@Component
public class HubValidatorImpl implements HubValidator {

    // TODO: Hub Service Feign Client 주입 필요

    @Override
    public boolean exists(HubId hubId) {
        if (hubId == null) return false;
        // 실제 구현: return hubClient.checkHubExists(hubId.getId());
        return true; // 테스트를 위해 무조건 true 반환
    }
}