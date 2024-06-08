package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.DistrictRepository;
import com.javaweb.repository.entity.DistrictEntity;

@Repository
public class DistrictRepositoryImpl implements DistrictRepository {
	static final String DB_URL = "jdbc:mysql://localhost:3306/estatebasic";
	static final String USER = "root";
	static final String PASS = "190502";

	@Override
	public List<DistrictEntity> getDistrictNames() {
		List<DistrictEntity> districtNames = new ArrayList<>();
		String sql = "SELECT id, name FROM district";

		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery(sql)) {

			while (rs.next()) {
				DistrictEntity districtEntity = new DistrictEntity();
				districtEntity.setId(rs.getLong("id"));
				districtEntity.setName(rs.getString("name"));
				districtNames.add(districtEntity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connected database failed: " + e.getMessage());
		}

		return districtNames;
	}
}
