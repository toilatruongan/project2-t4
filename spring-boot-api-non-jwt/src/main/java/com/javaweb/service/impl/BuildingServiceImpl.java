package com.javaweb.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaweb.dto.response.BuildingResponseDTO;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.DistrictRepository;
import com.javaweb.repository.RentAreaRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.entity.DistrictEntity;
import com.javaweb.repository.entity.RentAreaEntity;
import com.javaweb.service.BuildingService;

@Service
public class BuildingServiceImpl implements BuildingService {
    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private RentAreaRepository rentAreaRepository;

    @Override
    public List<BuildingResponseDTO> findAll(Map<String, Object> params, List<String> typecode) {
        List<BuildingEntity> buildingEntities = buildingRepository.findAll(params, typecode);
        List<BuildingResponseDTO> results = new ArrayList<>();
        List<DistrictEntity> districtNames = districtRepository.getDistrictNames();

        Map<Long, String> districtNameMap = districtNames.stream()
            .collect(Collectors.toMap(DistrictEntity::getId, DistrictEntity::getName));

        for (BuildingEntity item : buildingEntities) {
            BuildingResponseDTO buildingResponseDTO = new BuildingResponseDTO();
            buildingResponseDTO.setId(item.getId());
            String districtName = districtNameMap.get(item.getDistrictId());
            buildingResponseDTO.setAddress(item.getStreet() + "," + item.getWard() + "," + (districtName != null ? districtName : "Unknown"));
            buildingResponseDTO.setName(item.getName());
            buildingResponseDTO.setRentPrice(item.getRentPrice());
            buildingResponseDTO.setNumberOfBasement(item.getNumberOfBasement());
            buildingResponseDTO.setManagerName(item.getManagerName());
            buildingResponseDTO.setManagerPhoneNumber(item.getManagerPhoneNumber());
            buildingResponseDTO.setBrokerageFee(item.getBrokerageFee());
            buildingResponseDTO.setFloorArea(item.getFloorArea());

            List<RentAreaEntity> rentAreaEntities = rentAreaRepository.getRentAreasByBuildingId(item.getId());
            String rentAreas = rentAreaEntities.stream()
                .map(RentAreaEntity::getValue)
                .collect(Collectors.joining(","));
            buildingResponseDTO.setRentAreas(rentAreas);

            results.add(buildingResponseDTO);
        }
        return results;
    }
}
