package org.bangbanghub.deliverypersonservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class HubId {

    @Column(name = "hub_id", nullable = false, columnDefinition = "UUID")
    private UUID id;

    public HubId(UUID value) {
        this.id = value;
    }

    public static HubId of(UUID id) {
        return new HubId(id);
    }
}