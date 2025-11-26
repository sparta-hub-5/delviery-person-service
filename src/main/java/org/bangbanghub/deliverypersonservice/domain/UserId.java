package org.bangbanghub.deliverypersonservice.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserId {

    @Id
    private UUID id;

    public UserId(UUID id) {
        this.id = id;
    }
}
