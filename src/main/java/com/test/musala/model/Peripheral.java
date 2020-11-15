package com.test.musala.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.sql.Date;

@Entity
@Table(name = "peripheral")
public class Peripheral {
    private Long UID;
    @NotBlank(message = "Vendor empty")
    private String vendor;
    @NotBlank(message = "Status empty")
    private String status;
    private Date created;

    public Peripheral() {
    }

    public Peripheral(@NotBlank(message = "Vendor empty") String vendor, @NotBlank(message = "Status empty") String status, Date created) {
        this.vendor = vendor;
        this.status = status;
        this.created = created;
    }

    public Peripheral(Long UID, @NotEmpty(message = "Vendor empty") String vendor, Date created, @NotEmpty(message = "Status empty") String status) {
        this.UID = UID;
        this.vendor = vendor;
        this.created = created;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Override
    public String toString() {
        return new StringBuilder().append("UID: ").append(getUID())
                .append(" Vendor: ").append(getVendor())
                .append(" Created at: ").append(getCreated().toString())
                .append(" Status: ").append(getStatus()).toString();
    }
}
