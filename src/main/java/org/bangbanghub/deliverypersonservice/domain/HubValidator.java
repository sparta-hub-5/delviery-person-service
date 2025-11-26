package org.bangbanghub.deliverypersonservice.domain;

public interface HubValidator {
    /**
 * Determine whether a hub with the specified identifier exists.
 *
 * @param hubId the identifier of the hub to check
 * @return `true` if a hub with the given identifier exists, `false` otherwise
 */
boolean exists(HubId hubId);
}