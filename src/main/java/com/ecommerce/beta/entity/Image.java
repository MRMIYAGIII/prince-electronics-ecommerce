package com.ecommerce.beta.entity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Type(type = "org.hibernate.type.UUIDCharType")
	private UUID uuid;

	private String fileName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id_uuid")
	private Product product;

	public Image(String fileName, Product product) {
		this.fileName = fileName;
		this.product = product;
	}
}