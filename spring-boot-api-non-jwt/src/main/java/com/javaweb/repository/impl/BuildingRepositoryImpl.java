
package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.utils.ConnectionUtil;
import com.javaweb.utils.NumberUtil;
import com.javaweb.utils.StringUtil;

@Repository
public class BuildingRepositoryImpl implements BuildingRepository {

    private void querySqlJoin(Map<String, Object> params, List<String> typeCode, StringBuilder join) {
        String staffId = (String) params.get("staffId");
        if (StringUtil.checkData(staffId)) {
            join.append(" INNER JOIN assignmentbuilding ab ON b.id = ab.buildingid");
        }

        String rentAreaFrom = (String) params.get("rentAreaFrom");
        String rentAreaTo = (String) params.get("rentAreaTo");
        if (StringUtil.checkData(rentAreaFrom) || StringUtil.checkData(rentAreaTo)) {
            join.append(" INNER JOIN rentarea ra ON b.id = ra.buildingid");
        }

        if (typeCode != null && !typeCode.isEmpty()) {
            join.append(" INNER JOIN buildingrenttype brt ON b.id = brt.buildingid");
            join.append(" INNER JOIN renttype rt ON brt.renttypeid = rt.id");
        }
    }

    private void querySqlNormal(Map<String, Object> params, StringBuilder where) {
        for (Map.Entry<String, Object> item : params.entrySet()) {
            String key = item.getKey();
            if (!key.equals("staffId") && !key.equals("typeCode") && !key.startsWith("rentArea") && !key.startsWith("rentPrice")) {
                String value = item.getValue().toString();
                if (NumberUtil.isNumber(value)) {
                    where.append(" AND b.").append(key.toLowerCase()).append(" = ").append(value);
                } else {
                    where.append(" AND b.").append(key.toLowerCase()).append(" LIKE '%").append(value).append("%'");
                }
            }
        }
    }

    private void querySqlSpecial(Map<String, Object> params, List<String> typeCode, StringBuilder where) {
        String rentAreaFrom = (String) params.get("rentAreaFrom");
        String rentAreaTo = (String) params.get("rentAreaTo");
        String rentPriceFrom = (String) params.get("rentPriceFrom");
        String rentPriceTo = (String) params.get("rentPriceTo");

        if (StringUtil.checkData(rentAreaFrom)) {
            where.append(" AND ra.value >= ").append(rentAreaFrom);
        }
        if (StringUtil.checkData(rentAreaTo)) {
            where.append(" AND ra.value <= ").append(rentAreaTo);
        }

        if (StringUtil.checkData(rentPriceFrom)) {
            where.append(" AND b.rentprice >= ").append(rentPriceFrom);
        }
        if (StringUtil.checkData(rentPriceTo)) {
            where.append(" AND b.rentprice <= ").append(rentPriceTo);
        }
        
        if (typeCode != null && !typeCode.isEmpty()) {
            where.append(" AND (")
                .append(typeCode.stream().map(item -> "rt.code LIKE '%" + item + "%'").collect(Collectors.joining(" OR ")))
                .append(") ");
        }
    }

    @Override
    public List<BuildingEntity> findAll(Map<String, Object> params, List<String> typecode) {
        List<BuildingEntity> buildings = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT DISTINCT b.* FROM building b");
        StringBuilder joinClause = new StringBuilder();
        StringBuilder whereClause = new StringBuilder(" WHERE 1=1");

        querySqlJoin(params, typecode, joinClause);
        querySqlNormal(params, whereClause);
        querySqlSpecial(params, typecode, whereClause);

        sql.append(joinClause).append(whereClause);

        try (Connection conn = ConnectionUtil.getConnection();
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
