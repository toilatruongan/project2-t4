package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.RentTypeRepository;
import com.javaweb.repository.entity.BuildingEntity;

@Repository
public class BuildingRepositoryImpl implements BuildingRepository {

    static final String DB_URL = "jdbc:mysql://localhost:3306/estatebasic";
    static final String USER = "root";
    static final String PASS = "190502";

    @Autowired
    private RentTypeRepository rentTypeRepository;

    @Override
    public List<BuildingEntity> findAll(Map<String, Object> params) {
        List<BuildingEntity> buildings = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT b.* FROM building b");
        StringBuilder whereClause = new StringBuilder(" WHERE 1=1");
        boolean joinStaff = false;
        boolean joinRentArea = false;

        List<Long> buildingIdsByTypeCodes = new ArrayList<>();
        if (params.containsKey("typecode")) {
            Object typecodeParam = params.get("typecode");
            if (typecodeParam instanceof String) {
                buildingIdsByTypeCodes = rentTypeRepository.getBuildingIdsByTypeCodes(Collections.singletonList((String) typecodeParam));
            } else if (typecodeParam instanceof List) {
                buildingIdsByTypeCodes = rentTypeRepository.getBuildingIdsByTypeCodes((List<String>) typecodeParam);
            }

            if (!buildingIdsByTypeCodes.isEmpty()) {
                whereClause.append(" AND b.id IN (")
                           .append(buildingIdsByTypeCodes.stream().map(String::valueOf).collect(Collectors.joining(",")))
                           .append(")");
            }
        }

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (key.equals("staffId") && value != null) {
                joinStaff = true;
                whereClause.append(" AND ab.staffid = ").append(value);
            } else if (key.equals("districtId") && value != null) {
                whereClause.append(" AND b.districtid = ").append(value);
            } else if (key.equals("name") && value != null && !value.toString().isEmpty()) {
                whereClause.append(" AND b.name LIKE '%").append(value).append("%'");
            } else if (key.equals("numberOfBasement") && value != null) {
                whereClause.append(" AND b.numberofbasement = ").append(value);
            } else if (key.equals("rentPriceFrom") && value != null) {
                whereClause.append(" AND b.rentprice >= ").append(value);
            } else if (key.equals("rentPriceTo") && value != null) {
                whereClause.append(" AND b.rentprice <= ").append(value);
            } else if (key.equals("ward") && value != null && !value.toString().isEmpty()) {
                whereClause.append(" AND b.ward LIKE '%").append(value).append("%'");
            } else if (key.equals("street") && value != null && !value.toString().isEmpty()) {
                whereClause.append(" AND b.street LIKE '%").append(value).append("%'");
            } else if (key.equals("managerName") && value != null && !value.toString().isEmpty()) {
                whereClause.append(" AND b.managername LIKE '%").append(value).append("%'");
            } else if (key.equals("managerPhoneNumber") && value != null && !value.toString().isEmpty()) {
                whereClause.append(" AND b.managerphonenumber LIKE '%").append(value).append("%'");
            } else if (key.equals("level") && value != null && !value.toString().isEmpty()) {
                whereClause.append(" AND b.level LIKE '%").append(value).append("%'");
            } else if (key.equals("rentAreaFrom") && value != null) {
                joinRentArea = true;
                whereClause.append(" AND ra.value >= ").append(value);
            } else if (key.equals("rentAreaTo") && value != null) {
                joinRentArea = true;
                whereClause.append(" AND ra.value <= ").append(value);
            }
        }

        if (joinStaff) {
            sql.append(" JOIN assignmentbuilding ab ON b.id = ab.buildingid ");
        }

        if (joinRentArea) {
            sql.append(" JOIN rentarea ra ON b.id = ra.buildingid ");
        }

        sql.append(whereClause);

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery(sql.toString())) {

            while (rs.next()) {
                BuildingEntity buildingEntity = new BuildingEntity();
                buildingEntity.setId(rs.getLong("id"));
                buildingEntity.setName(rs.getString("name"));
                buildingEntity.setStreet(rs.getString("street"));
                buildingEntity.setWard(rs.getString("ward"));
                buildingEntity.setDistrictId(rs.getLong("districtid"));
                buildingEntity.setNumberOfBasement(rs.getInt("numberofbasement"));
                buildingEntity.setFloorArea(rs.getInt("floorarea"));
                buildingEntity.setRentPrice(rs.getInt("rentprice"));
                buildingEntity.setBrokerageFee(rs.getBigDecimal("brokeragefee"));
                buildingEntity.setManagerName(rs.getString("managername"));
                buildingEntity.setManagerPhoneNumber(rs.getString("managerphonenumber"));

                buildings.add(buildingEntity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connected database failed: " + e.getMessage());
        }

        return buildings;
    }
}
