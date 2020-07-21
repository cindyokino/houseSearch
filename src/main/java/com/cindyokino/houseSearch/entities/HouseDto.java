package com.cindyokino.houseSearch.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HouseDto {
	private Long id;
	private String address;
	private String city;
	private String neighborhood;
	private Long price;
}
