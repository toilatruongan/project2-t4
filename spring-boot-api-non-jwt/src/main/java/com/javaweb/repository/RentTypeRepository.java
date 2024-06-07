package com.javaweb.repository;

import java.util.List;

public interface RentTypeRepository {
    List<Long> getBuildingIdsByTypeCodes(List<String> typeCodes);
}