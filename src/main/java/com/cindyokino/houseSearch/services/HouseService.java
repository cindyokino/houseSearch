package com.cindyokino.houseSearch.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cindyokino.houseSearch.entities.House;
import com.cindyokino.houseSearch.repositories.HouseRepository;

@Service
public class HouseService {

	@Autowired
	private HouseRepository houseRepository;

	public List<House> findAll(Long minPrice, Long maxPrice) {
		return houseRepository.customMethod(minPrice, maxPrice);
	}

	public House findById(Long id) {
		Optional<House> obj = houseRepository.findById(id);
		return obj.orElseThrow(() -> new IllegalArgumentException("ERROR: Object not found at the database"));
	}

	public List<House> insert(List<House> houses) {
		houses.forEach(h -> {
			Optional<House> optionalFoundHouse = houseRepository.findById(h.getId());
			optionalFoundHouse.ifPresentOrElse(
					foundHouse -> h.setRegisteredOn(foundHouse.getRegisteredOn()), 
					() -> h.setRegisteredOn(LocalDate.now()));
			h.setUpdatedOn(LocalDate.now());
		});
		return houseRepository.saveAll(houses);
	}

	public House update(House house) {
		House houseInDatabase = houseRepository.findById(house.getId()).orElse(null);
		if (houseInDatabase == null) {
			System.out.println("ERROR: House to update not found");
			throw new IllegalArgumentException("House to update not found");
		}
		house.setRegisteredOn(houseInDatabase.getRegisteredOn());
		house.setUpdatedOn(LocalDate.now());
		return houseRepository.save(house);
	}

	public void deleteById(Long id) {
		if (!(findById(id) == null)) {
			houseRepository.deleteById(id);
		}
	}
}
