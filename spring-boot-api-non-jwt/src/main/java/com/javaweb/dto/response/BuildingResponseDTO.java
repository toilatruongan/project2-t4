package com.javaweb.dto.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuildingResponseDTO {
    private Long id;
    private String name;
    private String address;
    private Integer numberOfBasement;

    private String managerName;
    private String managerPhoneNumber;
    private BigDecimal brokerageFee; // Phí mối giới
    private Integer rentPrice;
    private Integer floorArea; // Diện tích sàn
    private Integer vacantArea; // Diện tích trống
    private List<Integer> rentAreas; // Diện tích thuê

}
