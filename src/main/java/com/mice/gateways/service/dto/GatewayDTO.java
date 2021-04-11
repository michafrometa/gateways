package com.mice.gateways.service.dto;

import com.openpojo.business.BusinessIdentity;
import com.openpojo.business.annotation.BusinessKey;

import java.io.Serializable;

public class GatewayDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @BusinessKey(composite = true, required = false)
    private String serialNumber;

    @BusinessKey(composite = true, required = false)
    private String name;

    @BusinessKey(composite = true, required = false)
    private String address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

        GatewayDTO object = (GatewayDTO) obj;

        return BusinessIdentity.areEqual(this, object);
    }

    public int hashCode() {
        return BusinessIdentity.getHashCode(this);
    }

    public String toString() {
        return BusinessIdentity.toString(this);
    }
}
