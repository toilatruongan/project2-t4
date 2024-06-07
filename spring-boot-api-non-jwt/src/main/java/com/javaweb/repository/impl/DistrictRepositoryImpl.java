package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.javaweb.dto.DistrictDTO;
import com.javaweb.repository.DistrictRepository;

@Repository
public class DistrictRepositoryImpl implements DistrictRepository {
    static final String DB_URL = "jdbc:mysql://localhost:3306/estatebasic";
    static final String USER = "root";
    static final String PASS = "190502";

    @Override
    public List<DistrictDTO> getDistrictNames() {
        List<DistrictDTO> districtNames = new ArrayList<>();
        String sql = "SELECT id, name FROM district";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {

            while (rs.next()) {
                DistrictDTO districtDTO = new DistrictDTO();
                districtDTO.setId(rs.getLong("id"));
                districtDTO.setName(rs.getString("name"));
                districtNames.add(districtDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connected database failed: " + e.getMessage());
        }

        return districtNames;
    }
}
