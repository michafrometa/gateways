package com.mice.gateways.domain;

import com.openpojo.business.BusinessIdentity;
import com.openpojo.business.annotation.BusinessKey;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A gateway
 */
@Entity
public class Gateway implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @BusinessKey(composite = true, required = false)
    private String serialNumber;

    @BusinessKey(composite = true, required = false)
    private String name;

    @Pattern(regexp = "^([0-1]?\\d?\\d|2[0-4]\\d|25[0-5])(\\.([0-1]?\\d?\\d|2[0-4]\\d|25[0-5])){3}$")
    @BusinessKey(composite = true, required = false)
    private String address;

    @OneToMany(mappedBy = "gateway", cascade = CascadeType.PERSIST)
    @Size(max = 10)
    private Set<Peripheral> peripherals = new HashSet<>();

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

    public Gateway serialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gateway name(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Gateway address(String address) {
        this.address = address;
        return this;
    }

    public Set<Peripheral> getPeripherals() {
        return peripherals;
    }

    public Gateway peripherals(Set<Peripheral> peripherals) {
        this.peripherals = peripherals;
        return this;
    }

    public Gateway addPeripheral(Peripheral peripheral) {
        this.peripherals.add(peripheral);
        peripheral.setGateway(this);
        return this;
    }

    public Gateway removePeripheral(Peripheral peripheral) {
        this.peripherals.remove(peripheral);
        peripheral.setGateway(null);
        return this;
    }

    public void setPeripherals(Set<Peripheral> peripherals) {
        this.peripherals = peripherals;
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

        Gateway object = (Gateway) obj;

        return BusinessIdentity.areEqual(this, object);
    }

    public int hashCode() {
        return BusinessIdentity.getHashCode(this);
    }

    public String toString() {
        return BusinessIdentity.toString(this);
    }
}
