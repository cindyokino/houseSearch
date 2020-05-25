package com.cindyokino.houseSearch.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

@RestController
@RequestMapping(value = "/houses")
public class HouseController {

	List<House> list = new ArrayList<>();
	House house1 = new House(14158660L, 200_000d, "2000, Rue Jean-Béliveau", "Longueuil (Le Vieux-Longueuil)",
			"Quartier Centre", LocalDate.now(), LocalDate.now());
	House house2 = new House(13262772L, 1_120_000d, "864, boulevard Queen", "Saint-Lambert (Montérégie)", null,
			LocalDate.now(), LocalDate.now());

	public HouseController() {
		list.add(house1);
		list.add(house2);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<House> findHouseById(@PathVariable Long id) {
		House house = list.stream().filter(h -> h.getId().equals(id)).findAny().get();
		return ResponseEntity.ok().body(house);
	}

	@GetMapping
	public ResponseEntity<List<House>> findHouses(
			@RequestParam(value = "minPrice", defaultValue = "") Double minPrice,
			@RequestParam(value = "maxPrice", defaultValue = "") Double maxPrice) {
		if(minPrice == null && maxPrice == null) {
			return ResponseEntity.ok().body(list);
		}
		List<House> houses = list.stream().filter(house -> {
			if(minPrice != null && maxPrice != null) {
				return house.getPrice() > minPrice && house.getPrice() < maxPrice;				
			}	
			else if(minPrice != null) {
				return house.getPrice() > minPrice;
			}
			else if(maxPrice != null) {
				return house.getPrice() < maxPrice;
			}
			return false;
		}).collect(Collectors.toList());
		return ResponseEntity.ok().body(houses);
	}

	@PostMapping
	public ResponseEntity<Void> insertHouse(@RequestBody List<House> houses) {
		houses.forEach(house -> {
			System.out.println(house.getId());
			System.out.println(house.getCity());
			System.out.println(house.getAddress());
			System.out.println(house.getPrice());
			System.out.println(house.getRegisteredOn());
			System.out.println(house.getUpdatedOn());
		});
		return ResponseEntity.accepted().build();
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<House> updateHouse(@PathVariable Long id, @RequestBody House obj) {
		House house = list.stream().filter(h -> h.getId().equals(id)).findAny().get();
		house.setAddress(obj.getAddress());
		house.setCity(obj.getCity());
		house.setId(obj.getId());
		house.setNeighborhood(obj.getNeighborhood());
		house.setPrice(obj.getPrice());
		house.setUpdatedOn(obj.getUpdatedOn());
		return ResponseEntity.ok().body(house);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteHouse(@PathVariable Long id) {
		House house = list.stream().filter(h -> h.getId().equals(id)).findAny().get();
		list.remove(house);
		return ResponseEntity.noContent().build();
	}
}
