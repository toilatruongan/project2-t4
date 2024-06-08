package com.javaweb.repository;

import java.util.List;

import com.javaweb.dto.DistrictDTO;
import com.javaweb.repository.entity.DistrictEntity;

public interface DistrictRepository {
    List<DistrictEntity> getDistrictNames();
}
