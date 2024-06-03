package com.javaweb.repository.entity;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuildingEntity {
    private Long id;
    private String name;
    private String street;
    private String ward;
    private Long districtId;
    private Integer numberOfBasement;
    private Integer floorArea;
    private Integer rentPrice;
    private BigDecimal brokerageFee;
    private String managerName;
    private String managerPhoneNumber;
    private List<Integer> rentAreas;

}
