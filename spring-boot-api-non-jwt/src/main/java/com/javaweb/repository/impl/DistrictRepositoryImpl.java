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
import com.javaweb.utils.ConnectionUtil;

@Repository
public class DistrictRepositoryImpl implements DistrictRepository {
	@Override
	public List<DistrictEntity> getDistrictNames() {
		List<DistrictEntity> districtNames = new ArrayList<>();
		String sql = "SELECT id, name FROM district";

		try (Connection conn = ConnectionUtil.getConnection();
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
