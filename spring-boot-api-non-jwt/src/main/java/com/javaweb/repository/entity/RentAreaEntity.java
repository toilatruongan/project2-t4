package com.javaweb.repository.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RentAreaEntity {
	private Long id;
	private Long buildingId;
	private String value;
}
