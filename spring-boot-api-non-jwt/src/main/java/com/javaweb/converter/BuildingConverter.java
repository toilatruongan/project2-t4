package com.javaweb.converter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.javaweb.dto.response.BuildingResponseDTO;
import com.javaweb.repository.DistrictRepository;
import com.javaweb.repository.RentAreaRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.entity.DistrictEntity;
import com.javaweb.repository.entity.RentAreaEntity;

@Component
public class BuildingConverter {
    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private RentAreaRepository rentAreaRepository;

    @Autowired
    private ModelMapper modelMapper;

    private Map<Long, String> districtNameMap;

    @Autowired
    public void init() {
        List<DistrictEntity> districtNames = districtRepository.getDistrictNames();
        districtNameMap = districtNames.stream()
            .collect(Collectors.toMap(DistrictEntity::getId, DistrictEntity::getName));
    }

    public BuildingResponseDTO toBuildingResponseDTO(BuildingEntity item) {
        BuildingResponseDTO buildingResponseDTO = modelMapper.map(item, BuildingResponseDTO.class);
        String districtName = districtNameMap.get(item.getDistrictId());
        buildingResponseDTO.setAddress(item.getStreet() + "," + item.getWard() + "," + (districtName != null ? districtName : "Unknown"));

        List<RentAreaEntity> rentAreaEntities = rentAreaRepository.getRentAreasByBuildingId(item.getId());
        String rentAreas = rentAreaEntities.stream()
            .map(RentAreaEntity::getValue)
            .collect(Collectors.joining(","));
        buildingResponseDTO.setRentAreas(rentAreas);

        return buildingResponseDTO;
    }
}
