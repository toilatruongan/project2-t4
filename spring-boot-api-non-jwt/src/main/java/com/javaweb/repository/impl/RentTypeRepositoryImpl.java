package com.javaweb.repository.impl;

import com.javaweb.repository.RentTypeRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RentTypeRepositoryImpl implements RentTypeRepository {
    static final String DB_URL = "jdbc:mysql://localhost:3306/estatebasic";
    static final String USER = "root";
    static final String PASS = "190502";

    @Override
    public List<Long> getBuildingIdsByTypeCodes(List<String> typeCodes) {
        List<Long> buildingIds = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT DISTINCT brt.buildingid FROM buildingrenttype brt ");
        sql.append("JOIN renttype rt ON brt.renttypeid = rt.id WHERE ");

        for (int i = 0; i < typeCodes.size(); i++) {
            sql.append("rt.code = '").append(typeCodes.get(i)).append("'");
            if (i < typeCodes.size() - 1) {
                sql.append(" OR ");
            }
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery(sql.toString())) {

            while (rs.next()) {
                buildingIds.add(rs.getLong("buildingid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connected database failed: " + e.getMessage());
        }

        return buildingIds;
    }
}
