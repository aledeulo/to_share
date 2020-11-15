package com.test.musala.model;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "gateway")
public class Gateway {

    @Id
    private String serialNumber;
    @NotBlank(message = "name can't be empty")
    private String name;
    @NotBlank(message = "ip address can't be empty")
    private String ipAddress;
    @ElementCollection
    private List<Long> peripheralIdlList;

    public Gateway() {
    }

    public Gateway(String serialNumber, @NotBlank(message = "name can't be empty") String name, @NotBlank(message = "ip address can't be empty") String ipAddress) {
        this.serialNumber = serialNumber;
        this.name = name;
        this.ipAddress = ipAddress;
    }

    public Gateway(String serialNumber, @NotBlank(message = "name can't be empty") String name, @NotBlank(message = "ip address can't be empty") String ipAddress, List<Long> peripheralIdlList) {
        this.serialNumber = serialNumber;
        this.name = name;
        this.ipAddress = ipAddress;
        this.peripheralIdlList = peripheralIdlList;
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

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public List<Long> getPeripheralIdlList() {
        return peripheralIdlList;
    }

    public void setPeripheralIdlList(List<Long> peripheralIdlList) {
        this.peripheralIdlList = peripheralIdlList;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("Serial number: ").append(getSerialNumber())
                .append(" Name: ").append(getName())
                .append(" IP address: ").append(getIpAddress())
                .append(" Devices: ").append(getPeripheralIdlList().toString()).toString();
    }
}
