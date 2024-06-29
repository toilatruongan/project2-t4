package com.javaweb.repository.impl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.javaweb.builder.BuildingSearchBuilder;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.utils.ConnectionUtil;

@Repository
@Primary // ưu tiên
public class BuildingRepositoryImpl implements BuildingRepository {
	@PersistenceContext
	private EntityManager entityManager;
	
    private void querySqlJoin(BuildingSearchBuilder builder, StringBuilder join) {
        Integer staffId = builder.getStaffId();
        if (staffId != null) {
            join.append(" INNER JOIN assignmentbuilding ab ON b.id = ab.buildingid");
        }

        Integer rentAreaFrom = builder.getRentAreaFrom();
        Integer rentAreaTo = builder.getRentAreaTo();
        if (rentAreaFrom != null || rentAreaTo != null) {
            join.append(" INNER JOIN rentarea ra ON b.id = ra.buildingid");
        }

        List<String> typeCode = builder.getTypeCode();
        if (typeCode != null && !typeCode.isEmpty()) {
            join.append(" INNER JOIN buildingrenttype brt ON b.id = brt.buildingid");
            join.append(" INNER JOIN renttype rt ON brt.renttypeid = rt.id");
        }
    }

    private void querySqlNormal(BuildingSearchBuilder builder, StringBuilder where) {
        Field[] fields = BuildingSearchBuilder.class.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = field.get(builder);
                if (value != null && !fieldName.startsWith("rentArea") && !fieldName.equals("staffId")&& !fieldName.startsWith("rentPrice")) {
                    if (field.getType().equals(Integer.class) || field.getType().equals(Long.class)) {
                        where.append(" AND b.").append(fieldName.toLowerCase()).append(" = ").append(value);
                    } else if (field.getType().equals(String.class)) {
                        where.append(" AND b.").append(fieldName.toLowerCase()).append(" LIKE '%").append(value).append("%'");
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void querySqlSpecial(BuildingSearchBuilder builder, StringBuilder where) {
        Integer rentAreaFrom = builder.getRentAreaFrom();
        Integer rentAreaTo = builder.getRentAreaTo();
        Integer rentPriceFrom = builder.getRentPriceFrom();
        Integer rentPriceTo = builder.getRentPriceTo();
        Integer staffId = builder.getStaffId();

        if (staffId != null) {
            where.append(" AND EXISTS (SELECT 1 FROM assignmentbuilding ab WHERE b.id = ab.buildingid AND ab.staffid = ")
                .append(staffId).append(")");
        }
        if (rentAreaFrom != null) {
            where.append(" AND EXISTS (SELECT 1 FROM rentarea ra WHERE b.id = ra.buildingid AND ra.value >= ")
                .append(rentAreaFrom).append(")");
        }
        if (rentAreaTo != null) {
            where.append(" AND EXISTS (SELECT 1 FROM rentarea ra WHERE b.id = ra.buildingid AND ra.value <= ")
                .append(rentAreaTo).append(")");
        }
        if (rentPriceFrom != null) {
            where.append(" AND b.rentprice >= ").append(rentPriceFrom);
        }
        if (rentPriceTo != null) {
            where.append(" AND b.rentprice <= ").append(rentPriceTo);
        }

        List<String> typeCode = builder.getTypeCode();
        if (typeCode != null && !typeCode.isEmpty()) {
            where.append(" AND EXISTS (SELECT 1 FROM buildingrenttype brt JOIN renttype rt ON brt.renttypeid = rt.id WHERE b.id = brt.buildingid AND (")
                .append(typeCode.stream().map(item -> "rt.code LIKE '%" + item + "%'").collect(Collectors.joining(" OR ")))
                .append("))");
        }
    }

    @Override
    @Transactional
    public List<BuildingEntity> findAll(BuildingSearchBuilder builder) {
        List<BuildingEntity> buildings = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT DISTINCT b.* FROM building b");
        StringBuilder joinClause = new StringBuilder();
        StringBuilder whereClause = new StringBuilder(" WHERE 1=1");

        querySqlJoin(builder, joinClause);
        querySqlNormal(builder, whereClause);
        querySqlSpecial(builder, whereClause);

        sql.append(joinClause).append(whereClause);

        Query query  = entityManager.createNativeQuery(sql.toString(), BuildingEntity.class);
        return query.getResultList();
    }
}
