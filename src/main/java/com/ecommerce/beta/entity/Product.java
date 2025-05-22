package com.ecommerce.beta.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product   {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "org.hibernate.type.UUIDCharType")
	private UUID uuid;
	
	private String name;
	
	@Lob
	private String description;
	
	private BigDecimal price;
	
	private  boolean enabled= true;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@OneToMany(mappedBy = "product")
	@ToString.Exclude
	private List<Image> images;
	
	private boolean deleted = false;

	 @OneToMany(mappedBy = "productId")
    @ToString.Exclude
    private List<Variant> variants;
}
