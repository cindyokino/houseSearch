package com.cindyokino.houseSearch.repositories;

import java.util.List;

import com.cindyokino.houseSearch.entities.House;

public interface CustomizedHouseRepository {

	List<House> customMethod(Double minPrice, Double maxPrice);
	
}
