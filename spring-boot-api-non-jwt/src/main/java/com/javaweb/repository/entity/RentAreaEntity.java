package com.javaweb.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table (name = "rentarea")
public class RentAreaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
//	@Column(name = "buildingid")
//	private Long buildingId;
	@Column(name = "value")
	private String value;
	
	@ManyToOne
	@JoinColumn(name = "buildingid")
	private BuildingEntity buildingEntity;
}
