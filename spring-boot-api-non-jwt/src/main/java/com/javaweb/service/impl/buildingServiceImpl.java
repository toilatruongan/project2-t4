package com.javaweb.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaweb.dto.response.BuildingResponseDTO;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.service.BuildingService;

@Service
public class buildingServiceImpl implements BuildingService {
	@Autowired
    private BuildingRepository buildingRepository;

    @Override
    public List<BuildingResponseDTO> findAll(Map<String, Object> params) {
        List<BuildingEntity> buildingEntities = buildingRepository.findAll(params);
        List<BuildingResponseDTO> results = new ArrayList<BuildingResponseDTO>();
        for (BuildingEntity item : buildingEntities){
            BuildingResponseDTO buildingResponseDTO = new BuildingResponseDTO();
            buildingResponseDTO.setId(item.getId());
            buildingResponseDTO.setName(item.getName());
            buildingResponseDTO.setAddress(item.getStreet() + "," + item.getWard() + "," + item.getDistrictId());
            buildingResponseDTO.setRentPrice(item.getRentPrice());
            buildingResponseDTO.setNumberOfBasement(item.getNumberOfBasement());
            buildingResponseDTO.setManagerName(item.getManagerName());
            buildingResponseDTO.setManagerPhoneNumber(item.getManagerPhoneNumber());
            buildingResponseDTO.setBrokerageFee(item.getBrokerageFee());
            buildingResponseDTO.setFloorArea(item.getFloorArea());
            buildingResponseDTO.setRentAreas(item.getRentAreas()); // Gán giá trị rentAreas
            results.add(buildingResponseDTO);
        }
        return results;
    }
}
