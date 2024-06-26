package com.javaweb.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaweb.converter.BuildingConverter;
import com.javaweb.dto.response.BuildingResponseDTO;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.service.BuildingService;

@Service
public class BuildingServiceImpl implements BuildingService {
    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private BuildingConverter buildingConverter;

    @Override
    public List<BuildingResponseDTO> findAll(Map<String, Object> params, List<String> typecode) {
        List<BuildingEntity> buildingEntities = buildingRepository.findAll(params, typecode);
        return buildingEntities.stream()
                .map(buildingConverter::toBuildingResponseDTO)
                .collect(Collectors.toList());
    }
}
