package com.javaweb.repository.impl;

import org.springframework.stereotype.Repository;
import com.javaweb.repository.DistrictRepository;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@Repository
public class DistrictRepositoryImpl implements DistrictRepository {
    static final String DB_URL = "jdbc:mysql://localhost:3306/estatebasic";
    static final String USER = "root";
    static final String PASS = "190502";

    public Map<Long, String> getDistrictNames() {
        Map<Long, String> districtNames = new HashMap<>();
        String sql = "SELECT id, name FROM district";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {

            while (rs.next()) {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                districtNames.put(id, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connected database failed: " + e.getMessage());
        }

        // In các giá trị của districtNames ra màn hình để kiểm tra
//        for (Map.Entry<Long, String> entry : districtNames.entrySet()) {
//            System.out.println("District ID: " + entry.getKey() + ", District Name: " + entry.getValue());
//        }

        return districtNames;
    }
}

