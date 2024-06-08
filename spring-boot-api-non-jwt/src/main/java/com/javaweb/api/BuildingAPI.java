package com.javaweb.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.dto.response.BuildingResponseDTO;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.service.BuildingService;

@RestController
public class BuildingAPI {

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private BuildingRepository buildingRepository;

    @GetMapping(value = "/api/buildings")
    private Object getBuilding(@RequestParam Map<String,Object> params,
    							@RequestParam(name="typecode", required = false)List<String>typecode) {

    	List<BuildingResponseDTO> results = buildingService.findAll(params, typecode);
        
        //List<BuildingEntity> results = buildingRepository.findAll(nameBuilding, numberOfBasement);
        return results;
    }
}