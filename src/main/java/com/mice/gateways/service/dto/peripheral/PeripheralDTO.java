package com.mice.gateways.service.dto.peripheral;

import com.mice.gateways.domain.enumeration.Status;
import com.openpojo.business.BusinessIdentity;
import com.openpojo.business.annotation.BusinessKey;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.UUID;

public class PeripheralDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(hidden = true)
    private UUID id;

    @BusinessKey(composite = true, required = false)
    @ApiModelProperty(example = "Musala Soft")
    private String vendor;

    @ApiModelProperty(hidden = true)
    private Long createdDate;

    @BusinessKey(composite = true, required = false)
    @ApiModelProperty(example = "ONLINE")
    private Status status;

    @ApiModelProperty(hidden = true)
    private Long gatewayId;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
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

        PeripheralDTO object = (PeripheralDTO) obj;

        return BusinessIdentity.areEqual(this, object);
    }

    public int hashCode() {
        return BusinessIdentity.getHashCode(this);
    }

    public String toString() {
        return BusinessIdentity.toString(this);
    }
}
