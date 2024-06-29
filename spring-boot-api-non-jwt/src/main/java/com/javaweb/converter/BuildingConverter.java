package com.javaweb.converter;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.javaweb.dto.response.BuildingResponseDTO;
import com.javaweb.repository.entity.BuildingEntity;

@Component
public class BuildingConverter {

    @Autowired
    private ModelMapper modelMapper;

    public BuildingResponseDTO toBuildingResponseDTO(BuildingEntity entity) {
        BuildingResponseDTO buildingResponseDTO = modelMapper.map(entity, BuildingResponseDTO.class);
        buildingResponseDTO.setAddress(entity.getStreet() + " " + entity.getWard() + " " + entity.getDistrict().getName());
        buildingResponseDTO.setRentAreas(entity.getRentAreas().stream()
            .map(rentArea -> rentArea.getValue().toString())
            .collect(Collectors.joining(", ")));
        return buildingResponseDTO;
    }
}
