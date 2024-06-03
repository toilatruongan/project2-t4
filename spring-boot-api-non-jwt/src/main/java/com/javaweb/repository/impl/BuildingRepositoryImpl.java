package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.RentAreaRepository;
import com.javaweb.repository.entity.BuildingEntity;

@Repository
public class BuildingRepositoryImpl implements BuildingRepository {

	static final String DB_URL = "jdbc:mysql://localhost:3306/estatebasic";
	static final String USER = "root";
	static final String PASS = "190502";

	@Autowired
	private RentAreaRepository rentAreaRepository;

	@Override
	public List<BuildingEntity> findAll(Map<String, Object> params) {
		List<BuildingEntity> buildingEntities = new ArrayList<>();
		StringBuilder sql = new StringBuilder("SELECT b.* FROM building b WHERE 1=1");

		params.forEach((key, value) -> {
			if (key.equals("name") && value != null && !value.toString().isEmpty()) {
				sql.append(" AND b.name LIKE '%").append(value).append("%'");
			} else if (key.equals("numberOfBasement") && value != null) {
				sql.append(" AND b.numberofbasement = ").append(value);
			} else if (key.equals("rentPriceFrom") && value != null) {
				sql.append(" AND b.rentprice >= ").append(value);
			} else if (key.equals("rentPriceTo") && value != null) {
				sql.append(" AND b.rentprice <= ").append(value);
			} else if (key.equals("floorAreaFrom") && value != null) {
				sql.append(" AND b.floorarea >= ").append(value);
			} else if (key.equals("floorAreaTo") && value != null) {
				sql.append(" AND b.floorarea <= ").append(value);
			} else if (key.equals("ward") && value != null && !value.toString().isEmpty()) {
				sql.append(" AND b.ward LIKE '%").append(value).append("%'");
			} else if (key.equals("street") && value != null && !value.toString().isEmpty()) {
				sql.append(" AND b.street LIKE '%").append(value).append("%'");
			} else if (key.equals("managerName") && value != null && !value.toString().isEmpty()) {
				sql.append(" AND b.managername LIKE '%").append(value).append("%'");
			} else if (key.equals("managerPhoneNumber") && value != null && !value.toString().isEmpty()) {
				sql.append(" AND b.managerphonenumber LIKE '%").append(value).append("%'");
			} else if (key.equals("districtId") && value != null) {
				sql.append(" AND b.districtid = ").append(value);
			} else if (key.equals("typecode") && value instanceof List) {
				List<String> typecodes = (List<String>) value;
				if (!typecodes.isEmpty()) {
					sql.append(" AND rt.code IN (");
					for (int i = 0; i < typecodes.size(); i++) {
						sql.append("'").append(typecodes.get(i)).append("'");
						if (i < typecodes.size() - 1) {
							sql.append(", ");
						}
					}
					sql.append(")");
				}
			}
		});

		try (

				Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
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
				// Gán danh sách rentarea cho buildingEntity
				List<Integer> rentAreas = rentAreaRepository.getRentAreasByBuildingId(buildingEntity.getId());
				buildingEntity.setRentAreas(rentAreas);

				buildingEntities.add(buildingEntity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connected database failed: " + e.getMessage());
		}
		return buildingEntities;
	}
}
