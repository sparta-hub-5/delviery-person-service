package org.bangbanghub.deliverypersonservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class UserId implements Serializable {

    @Column(name = "user_id", columnDefinition = "UUID")
    private UUID id;

    public UserId(UUID id) {
        this.id = id;
    }
}