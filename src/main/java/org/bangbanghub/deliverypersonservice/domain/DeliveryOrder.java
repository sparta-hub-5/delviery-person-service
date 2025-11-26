package org.bangbanghub.deliverypersonservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class DeliveryOrder implements Comparable<DeliveryOrder> {

    @Column(name = "delivery_order", nullable = false, unique = true)
    private Long value;

    /**
     * Creates a DeliveryOrder with the specified delivery sequence number.
     *
     * @param value the delivery sequence number; must be greater than 0
     * @throws IllegalArgumentException if {@code value} is {@code null} or less than or equal to 0
     */
    private DeliveryOrder(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("배");
        }
        this.value = value;
    }

    public static DeliveryOrder of(Long value) {
        return new DeliveryOrder(value);
    }

    public static DeliveryOrder first() {
        return new DeliveryOrder(1L);
    }

    public DeliveryOrder next() {
        return new DeliveryOrder(this.value + 1);
    }

    @Override
    public int compareTo(DeliveryOrder other) {
        return this.value.compareTo(other.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}