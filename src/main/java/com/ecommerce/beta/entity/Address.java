package com.ecommerce.beta.entity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@ToString(exclude = {"userInfo"})
public class Address{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID uuid;

    private String flat;
    private String area;
    private String town;
    private String city;
    private String state;
    private String pin;
    private String landmark;
    private boolean defaultAddress;
    private boolean enabled = true;

    @ManyToOne
    @JoinColumn(name="user_id")
    private UserInfo userInfo;

    //overriding hashCode to remove circular dependency and stack overflow error

//    @Override
//    public int hashCode() {
//        // Exclude fields contributing to the circular reference
//        return Objects.hash(uuid, flat, area, town, city, state, pin, landmark);
//    }

    public Address() {
        this.setFlat(" ");
        this.setArea(" ");
        this.setTown(" ");
        this.setCity(" ");
        this.setState(" ");
        this.setPin(" ");
        this.setLandmark(" ");
        this.defaultAddress= true;
    }
}
