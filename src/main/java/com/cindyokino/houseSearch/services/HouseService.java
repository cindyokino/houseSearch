package com.cindyokino.houseSearch.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cindyokino.houseSearch.entities.House;
import com.cindyokino.houseSearch.repositories.HouseRepository;

@Service
public class HouseService {
	
	@Autowired
	private HouseRepository repository;
	
	public List<House> findAll() { 
		return repository.findAll();
	}

}
