package com.javaweb.converter;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.javaweb.builder.BuildingSearchBuilder;
import com.javaweb.utils.MapUtil;

@Component
public class BuildingSearchBuilderConverter {
    public BuildingSearchBuilder toBuildingSearchBuilder(Map<String, Object> params, List<String> typeCode) {
        BuildingSearchBuilder builder = new BuildingSearchBuilder.Builder()
            .setName(MapUtil.getObject(params, "name", String.class))
            .setFloorArea(MapUtil.getObject(params, "floorArea", Integer.class))
            .setWard(MapUtil.getObject(params, "ward", String.class))
            .setStreet(MapUtil.getObject(params, "street", String.class))
            .setDistrictId(MapUtil.getObject(params, "districtId", Long.class))
            .setNumberOfBasement(MapUtil.getObject(params, "numberOfBasement", Integer.class))
            .setTypeCode(typeCode)
            .setManagerName(MapUtil.getObject(params, "managerName", String.class))
            .setManagerPhoneNumber(MapUtil.getObject(params, "managerPhoneNumber", String.class))
            .setRentAreaFrom(MapUtil.getObject(params, "rentAreaFrom", Integer.class))
            .setRentAreaTo(MapUtil.getObject(params, "rentAreaTo", Integer.class))
            .setRentPriceFrom(MapUtil.getObject(params, "rentPriceFrom", Integer.class))
            .setRentPriceTo(MapUtil.getObject(params, "rentPriceTo", Integer.class))
            .setStaffId(MapUtil.getObject(params, "staffId", Integer.class))
            .setLevel(MapUtil.getObject(params, "level", Long.class))
            .build();
        return builder;
    }
}
