package com.javaweb.service;

import java.util.List;
import java.util.Map;

import com.javaweb.dto.response.BuildingResponseDTO;

public interface BuildingService {
    //List<BuildingResponseDTO> findAll (String nameBuilding, Integer numberOfBasement);
    List<BuildingResponseDTO> findAll (Map<String, Object> params);

}
