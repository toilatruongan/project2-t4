package com.javaweb.repository.impl;

import com.javaweb.repository.RentAreaRepository;
import com.javaweb.repository.entity.RentAreaEntity;
import com.javaweb.utils.ConnectionUtil;

import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RentAreaRepositoryImpl implements RentAreaRepository {

    @Override
    public List<RentAreaEntity> getRentAreasByBuildingId(Long buildingId) {
        List<RentAreaEntity> rentAreas = new ArrayList<>();
        String sql = "SELECT id, buildingid, value FROM rentarea WHERE buildingid = ?";

        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, buildingId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RentAreaEntity rentArea = new RentAreaEntity();
                    rentArea.setId(rs.getLong("id"));
                    rentArea.setBuildingId(rs.getLong("buildingid"));
                    rentArea.setValue(rs.getString("value"));
                    rentAreas.add(rentArea);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connected database failed: " + e.getMessage());
        }
        return rentAreas;
    }
}
