package com.javaweb.repository.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "building")
public class BuildingEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Column(name = "name")
    private String name;
	@Column(name = "street")
    private String street;
	@Column(name = "ward")
    private String ward;
//	@Column(name = "districtid")
//    private Long districtId;
	@ManyToOne
	@JoinColumn(name = "districtid")
	private DistrictEntity district;
	
	@OneToMany(mappedBy = "buildingEntity", fetch = FetchType.LAZY)
	private List<RentAreaEntity> rentAreas = new ArrayList<RentAreaEntity>();
	
	@Column(name = "numberofbasement")
    private Integer numberOfBasement;
	@Column(name = "floorarea")
    private Integer floorArea;
	@Column(name = "rentprice")
    private Integer rentPrice;
	@Column(name = "brokeragefee")
    private BigDecimal brokerageFee;
	@Column(name = "managername")
    private String managerName;
	@Column(name = "managerphonenumber")
    private String managerPhoneNumber;
//	@Column(name = "rentareas")
//    private String rentAreas;

}
