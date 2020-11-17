package com.test.musala.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "gateway")
public class Gateway {

    @Id
    @Column(name = "id")
    private String serialNumber;
    @Column(name = "name")
    private String name;
    @Column(name = "ipAddress")
    private String ipAddress;
    @OneToMany(mappedBy = "gateway", cascade = {CascadeType.ALL})
    private List<Peripheral> peripheralIdlList;

    public Gateway() {
    }

    public Gateway(String serialNumber, String name, String ipAddress) {
        this.serialNumber = serialNumber;
        this.name = name;
        this.ipAddress = ipAddress;
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

    public List<Peripheral> getPeripheralIdlList() {
        return peripheralIdlList;
    }

    public void setPeripheralIdlList(List<Peripheral> peripheralIdlList) {
        this.peripheralIdlList = peripheralIdlList;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("serialNumber:").append(getSerialNumber())
                .append(", name:").append(getName())
                .append(", ipAddress: ").append(getIpAddress()).append("peripheralIdlList").append(getPeripheralIdlList()).toString();
    }
}
