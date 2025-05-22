package com.ecommerce.beta.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {

    @Column(updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    private Date modifiedAt;

    @Column(name = "deleted_by", columnDefinition = "VARCHAR(255)")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID deletedBy;
    private boolean deleted = false;

    private Date deletedAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = new Date();
        }
    }

    public String getFormattedCreatedAt() {
        Date now = this.createdAt;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(now);
    }
}