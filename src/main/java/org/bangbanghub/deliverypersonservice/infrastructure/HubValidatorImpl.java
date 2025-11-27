package org.bangbanghub.deliverypersonservice.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bangbanghub.deliverypersonservice.domain.HubId;
import org.bangbanghub.deliverypersonservice.domain.HubValidator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubValidatorImpl implements HubValidator {

    private final HubServiceClient hubServiceClient;

    @Override
    public boolean exists(HubId hubId) {
        if (hubId == null || hubId.getId() == null) {
            return false;
        }

        try {
            return hubServiceClient.checkHubExists(hubId.getId());
        } catch (Exception e) {
            log.error("Hub Service 호출 중 오류 발생: hubId={}", hubId.getId(), e);

            return false;
        }
    }
}