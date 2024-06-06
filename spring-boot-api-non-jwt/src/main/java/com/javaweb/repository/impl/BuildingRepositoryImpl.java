package com.javaweb.repository.impl;

import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

@Repository
public class BuildingRepositoryImpl implements BuildingRepository {

    static final String DB_URL = "jdbc:mysql://localhost:3306/estatebasic";
    static final String USER = "root";
    static final String PASS = "190502";

    @Override
    public List<BuildingEntity> findAll(Map<String, Object> params) {
        Map<Long, BuildingEntity> buildingMap = new LinkedHashMap<>();
        StringBuilder sql = new StringBuilder("SELECT b.* FROM building b");
        StringBuilder joinClause = new StringBuilder();
        StringBuilder whereClause = new StringBuilder(" WHERE 1=1");

        boolean joinRentType = false;
        boolean joinStaff = false;
        boolean joinRentArea = false;

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (key.equals("typecode") && value instanceof List) {
                joinRentType = true;
                List<String> typecodes = (List<String>) value;
                if (!typecodes.isEmpty()) {
                    whereClause.append(" AND (");
                    for (int i = 0; i < typecodes.size(); i++) {
                        whereClause.append("rt.code LIKE '%").append(typecodes.get(i)).append("%'");
                        if (i < typecodes.size() - 1) {
                            whereClause.append(" OR ");
                        }
                    }
                    whereClause.append(")");
                }
            } else if (key.equals("staffId") && value != null) {
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
            } else if (key.equals("rentAreaFrom") && value != null) {
                joinRentArea = true;
                whereClause.append(" AND ra.value >= ").append(value);
            } else if (key.equals("rentAreaTo") && value != null) {
                joinRentArea = true;
                whereClause.append(" AND ra.value <= ").append(value);
            }
        }

        if (joinRentType) {
            joinClause.append(" JOIN buildingrenttype brt ON b.id = brt.buildingid ");
            joinClause.append(" JOIN renttype rt ON brt.renttypeid = rt.id ");
        }

        if (joinStaff) {
            joinClause.append(" JOIN assignmentbuilding ab ON b.id = ab.buildingid ");
        }

        if (joinRentArea) {
            joinClause.append(" JOIN rentarea ra ON b.id = ra.buildingid ");
        }

        sql.append(joinClause).append(whereClause);

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery(sql.toString())) {

            while (rs.next()) {
                Long id = rs.getLong("id");
                if (!buildingMap.containsKey(id)) {
                    BuildingEntity buildingEntity = new BuildingEntity();
                    buildingEntity.setId(id);
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

                    buildingMap.put(id, buildingEntity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connected database failed: " + e.getMessage());
        }

        return new ArrayList<>(buildingMap.values());
    }
}
