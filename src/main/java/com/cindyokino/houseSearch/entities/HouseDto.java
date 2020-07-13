package com.cindyokino.houseSearch.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HouseDto {
	private Long id;
	private String address;
	private String city;
	private String neighborhood;
	private Long price;

}
