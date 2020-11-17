package com.test.musala.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "peripheral")
public class Peripheral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UID;
    @Column(name = "vendor")
    private String vendor;
    @Column(name = "status")
    private String status;
    @Column(name = "created")
    private Date created;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "gateway_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Gateway gateway;

    public Peripheral() {
    }

    public Peripheral(String vendor, String status, Date created) {
        this.vendor = vendor;
        this.status = status;
        this.created = created;
    }

    public Peripheral(Long UID, String vendor, Date created, String status) {
        this.UID = UID;
        this.vendor = vendor;
        this.created = created;
        this.status = status;
    }

    public Long getUID() {
        return UID;
    }

    public void setUID(Long UID) {
        this.UID = UID;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("UID: ").append(getUID())
                .append(" Vendor: ").append(getVendor())
                .append(" Created at: ").append(getCreated().toString())
                .append(" Status: ").append(getStatus()).toString();
    }
}
