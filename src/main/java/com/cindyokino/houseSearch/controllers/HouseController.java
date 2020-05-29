package com.cindyokino.houseSearch.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cindyokino.houseSearch.entities.House;
import com.cindyokino.houseSearch.services.HouseService;

@RestController
@RequestMapping(value = "/houses")
public class HouseController {
	List<House> list = new ArrayList<>();
	
	@Autowired
	private HouseService houseService;

	@GetMapping(value = "/{id}")
	public ResponseEntity<House> findHouseById(@PathVariable Long id) {
		House obj = houseService.findById(id);
		return ResponseEntity.ok().body(obj);
	}
	@GetMapping
	public ResponseEntity<List<House>> findHouses(
			@RequestParam(value = "minPrice", defaultValue = "") Long minPrice,
			@RequestParam(value = "maxPrice", defaultValue = "") Long maxPrice) {		
		List<House> houses = houseService.findAll(minPrice, maxPrice);
		return ResponseEntity.ok().body(houses);
	}

	@PostMapping
	public ResponseEntity<Void> insertHouse(@RequestBody List<House> houses) {
		houses = houseService.insert(houses);
		return ResponseEntity.accepted().build();
	}

	@PutMapping
	public ResponseEntity<House> updateHouse(@RequestBody House house) {
		House updatedHouse;
		try {
			updatedHouse = houseService.update(house);
		}catch(IllegalArgumentException exception) {
			return ResponseEntity.badRequest().build();
		}
		
		return ResponseEntity.ok().body(updatedHouse);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteHouse(@PathVariable Long id) {
		houseService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
