package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.RentAreaRepository;

@Repository
public class RentAreaRepositoryImpl implements RentAreaRepository{
	static final String DB_URL = "jdbc:mysql://localhost:3306/estatebasic";
    static final String USER = "root";
    static final String PASS = "190502";
	@Override
	public List<Integer> getRentAreasByBuildingId(Long buildingId) {
		List<Integer> rentAreas = new ArrayList<>();
        String sql = "SELECT value FROM rentarea WHERE buildingid = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, buildingId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rentAreas.add(rs.getInt("value"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connected database failed: " + e.getMessage());
        }
        return rentAreas;
	}
    
}
