package com.cindyokino.houseSearch.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cindyokino.houseSearch.entities.House;

@RestController
@RequestMapping(value="/houses")
public class HouseController {
	
	List<House> list = new ArrayList<>();
	House house1 = new House(14158660L, 288800d, "2000, Rue Jean-Béliveau", "Longueuil (Le Vieux-Longueuil)", "Quartier Centre", LocalDate.now());
	House house2 = new House(13262772L, 1120000d, "864, boulevard Queen", "Saint-Lambert (Montérégie)", null, LocalDate.now());
	public HouseController() {		
		list.add(house1);
		list.add(house2);
	}
	
	@GetMapping
	public ResponseEntity<List<House>> findAllHouses(){		
		return ResponseEntity.ok(list);
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<House> findHouseById(@PathVariable Long id){
		House house = list.stream().filter(h -> h.getId().equals(id)).findAny().get();
		return ResponseEntity.ok().body(house);
	}
	
	@PostMapping
	public ResponseEntity<Void> insertHouse(@RequestBody List<House> houses) {
	houses.forEach(house -> {
		System.out.println(house.getId());
		System.out.println(house.getCity());
		System.out.println(house.getAddress());
		System.out.println(house.getPrice());
		System.out.println(house.getRegisteredOn());
	});
		return ResponseEntity.accepted().build();
	}
	
}
