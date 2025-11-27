package org.bangbanghub.deliverypersonservice.infrastructure;

import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "hub-service")
public interface HubServiceClient {
    @GetMapping("/hubs/{hubId}/exists")
    boolean checkHubExists(@PathVariable("hubId") UUID hubId);
}