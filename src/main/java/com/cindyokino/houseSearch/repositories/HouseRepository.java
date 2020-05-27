package com.cindyokino.houseSearch.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cindyokino.houseSearch.entities.House;

public interface HouseRepository extends JpaRepository<House, Long>, CustomizedHouseRepository {

}
