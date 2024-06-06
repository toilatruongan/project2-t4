package com.javaweb.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaweb.dto.response.BuildingResponseDTO;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.DistrictRepository;
import com.javaweb.repository.RentAreaRepository;
import com.javaweb.repository.entity.BuildingEntity;
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
	public List<BuildingResponseDTO> findAll(Map<String, Object> params) {
		List<BuildingEntity> buildingEntities = buildingRepository.findAll(params);
		List<BuildingResponseDTO> results = new ArrayList<>();
		Map<Long, String> districtNames = districtRepository.getDistrictNames();

		Integer rentAreaFrom = params.containsKey("rentAreaFrom")
				? Integer.parseInt(params.get("rentAreaFrom").toString())
				: null;
		Integer rentAreaTo = params.containsKey("rentAreaTo") ? Integer.parseInt(params.get("rentAreaTo").toString())
				: null;

		for (BuildingEntity item : buildingEntities) {
			List<Integer> rentAreas = rentAreaRepository.getRentAreasByBuildingId(item.getId());
			int totalRentArea = rentAreas.stream().mapToInt(Integer::intValue).sum();

			boolean addBuilding = true;
			if (rentAreaFrom != null && totalRentArea < rentAreaFrom) {
				addBuilding = false;
			}
			if (rentAreaTo != null && totalRentArea > rentAreaTo) {
				addBuilding = false;
			}

			if (addBuilding) {
				BuildingResponseDTO buildingResponseDTO = new BuildingResponseDTO();
				buildingResponseDTO.setId(item.getId());
				buildingResponseDTO.setName(item.getName());
				String districtName = districtNames.get(item.getDistrictId());

				buildingResponseDTO.setAddress(item.getStreet() + "," + item.getWard() + ","
						+ (districtName != null ? districtName : "Unknown"));
				buildingResponseDTO.setRentPrice(item.getRentPrice());
				buildingResponseDTO.setNumberOfBasement(item.getNumberOfBasement());
				buildingResponseDTO.setManagerName(item.getManagerName());
				buildingResponseDTO.setManagerPhoneNumber(item.getManagerPhoneNumber());
				buildingResponseDTO.setBrokerageFee(item.getBrokerageFee());
				buildingResponseDTO.setFloorArea(item.getFloorArea());
				buildingResponseDTO.setRentAreas(rentAreas);

				results.add(buildingResponseDTO);
			}
		}
		return results;
	}
}
