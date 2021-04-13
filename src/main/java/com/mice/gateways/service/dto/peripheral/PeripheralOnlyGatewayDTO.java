package com.mice.gateways.service.dto.peripheral;

import com.openpojo.business.BusinessIdentity;

import java.io.Serializable;
import java.util.UUID;

public class PeripheralOnlyGatewayDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private Long gatewayId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(Long gatewayId) {
        this.gatewayId = gatewayId;
    }

    /**
     * @param obj The object to compare
     * @return true if they are equal
     * <p>
     * Rewritten as https://rules.sonarsource.com/java/RSPEC-2097
     * to avoid Warning: 'equals()' should check the class of its parameter
     */
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (this.getClass() != obj.getClass())
            return false;

        PeripheralOnlyGatewayDTO object = (PeripheralOnlyGatewayDTO) obj;

        return BusinessIdentity.areEqual(this, object);
    }

    public int hashCode() {
        return BusinessIdentity.getHashCode(this);
    }

    public String toString() {
        return BusinessIdentity.toString(this);
    }
}
