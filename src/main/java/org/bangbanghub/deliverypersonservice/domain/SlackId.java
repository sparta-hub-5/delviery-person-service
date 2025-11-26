package org.bangbanghub.deliverypersonservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SlackId {

    @Column(name = "slack_id", nullable = false)
    private String id;

    public SlackId(String id) {
        this.id = id;
    }

    public static SlackId of(String id) {
        return new SlackId(id);
    }
}
