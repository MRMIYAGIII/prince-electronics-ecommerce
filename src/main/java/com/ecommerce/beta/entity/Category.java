package com.ecommerce.beta.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Type(type = "org.hibernate.type.UUIDCharType")
	private UUID uuid;

	@Column(unique = true)
	private String name;

	private String description;

    // Getter and setter for slug (already provided by Lombok, but explicit for clarity)
    @Getter
    @Setter
    @Column(unique = true)
	private String slug; // New field for slug

	@OneToMany(mappedBy = "category")
	@ToString.Exclude
	private List<Product> products = new ArrayList<>();

}