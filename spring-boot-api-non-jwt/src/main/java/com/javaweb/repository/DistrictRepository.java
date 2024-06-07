package com.javaweb.repository;

import java.util.List;

import com.javaweb.dto.DistrictDTO;

public interface DistrictRepository {
    List<DistrictDTO> getDistrictNames();
}
