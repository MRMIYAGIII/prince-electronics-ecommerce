package com.ecommerce.beta.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.ecommerce.beta.enums.OrderStatus;
import com.ecommerce.beta.enums.OrderType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID uuid;

    private Float total = 0F;

    private Float tax = 0F;

    @Transient
    private Float gross;

    private Float offPrice = 0F;

    private OrderStatus orderStatus = OrderStatus.UNKNOWN;

    private OrderType orderType = OrderType.UNKNOWN;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private UserInfo userInfo;

    @OneToMany(mappedBy = "orderHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<OrderItems> items = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    @ToString.Exclude
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(name = "address_id")
    @ToString.Exclude
    private Address userAddress;

    // Custom getters remain unchanged
    public Float getGross() {
        return total + tax;
    }

    public Long getGrossLong() {
        if (offPrice != null) {
            return (long) Math.round(total + tax);
        } else if (total == null) {
            return 0L;
        } else {
            return (long) Math.round(total + tax);
        }
    }

    public Float getOff() {
        if (offPrice == null) {
            return 0F;
        } else {
            return offPrice;
        }
    }
}