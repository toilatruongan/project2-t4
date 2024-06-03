package com.javaweb.repository;

import java.util.List;

public interface RentAreaRepository {
	 public List<Integer> getRentAreasByBuildingId(Long buildingId);
}
