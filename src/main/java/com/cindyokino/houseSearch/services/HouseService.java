package com.cindyokino.houseSearch.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cindyokino.houseSearch.entities.House;
import com.cindyokino.houseSearch.entities.HouseDto;
import com.cindyokino.houseSearch.entities.PriceHistory;
import com.cindyokino.houseSearch.repositories.HouseRepository;
import com.cindyokino.houseSearch.repositories.PriceHistoryRepository;

@Service
public class HouseService {

	@Autowired
	private HouseRepository houseRepository;

	@Autowired
	private PriceHistoryRepository priceHistoryRepository;

	public List<House> findAll(Long minPrice, Long maxPrice) {
		return houseRepository.findHousesByPriceRange(minPrice, maxPrice);
	}

	public House findById(Long id) {
		Optional<House> obj = houseRepository.findById(id);
		return obj.orElseThrow(() -> new IllegalArgumentException("ERROR: Object not found at the database"));
	}

	@Transactional
	public List<House> insert(List<HouseDto> houseDtos) {
		return houseDtos.stream().map(houseDto -> {
			House house = mapToHouse(houseDto);
			return this.save(house, houseDto.getPrice());
		}).collect(Collectors.toList());
	}
	
	@Transactional
	public House update(HouseDto houseDto) {		
		House house = mapToHouse(houseDto);
		
		Optional<House> optionalFoundHouse = houseRepository.findById(house.getId());
		if (optionalFoundHouse.isPresent()) {
			House houseInDb = optionalFoundHouse.get();
			update(house, houseInDb, houseDto.getPrice());
		} else {
			System.out.println("ERROR: House to update not found");
			throw new IllegalArgumentException("House to update not found");
		}

		return house;
	}

	public void deleteById(Long id) {
		if (!(findById(id) == null)) {
			houseRepository.deleteById(id);
		}
	}
	
	private House mapToHouse(HouseDto dto) {
		House house = new House();
		house.setAddress(dto.getAddress());
		house.setCity(dto.getCity());
		house.setNeighborhood(dto.getNeighborhood());
		house.setId(dto.getId());
		
		return house;
	}

	private House save(House house, Long price) {
		Optional<House> optionalFoundHouse = houseRepository.findById(house.getId());
		if (optionalFoundHouse.isPresent()) {
			House houseInDb = optionalFoundHouse.get();
			update(house, houseInDb, price);
		} else {
			house.setRegisteredOn(LocalDate.now());
	
			PriceHistory priceHistory = new PriceHistory();
			priceHistory.setHouse(house);
			priceHistory.setPrice(price);
			priceHistory.setUpdatedOn(LocalDateTime.now());
	
			house.setPriceHistory(Collections.singletonList(priceHistory));
	
			houseRepository.saveAndFlush(house);
			priceHistoryRepository.save(priceHistory);
		}
	
		return house;
	}

	private House update(House houseToUpdate, House houseInDb, Long price) {
			houseToUpdate.setRegisteredOn(houseInDb.getRegisteredOn());
			houseToUpdate.setPriceHistory(houseInDb.getPriceHistory());
	
			houseInDb.getPriceHistory().stream()
	//				.sorted(Comparator.comparing(PriceHistory::getUpdatedOn).reversed()) // igual a linha abaixo
					.sorted(Comparator.comparing(pricehist -> pricehist.getUpdatedOn(), Comparator.reverseOrder()))
					.findFirst()
					.filter(priceHist -> !priceHist.getPrice().equals(price))
					.ifPresent(priceHist -> {
						PriceHistory priceHistory = new PriceHistory();
						priceHistory.setHouse(houseToUpdate);
						priceHistory.setPrice(price);
						priceHistory.setUpdatedOn(LocalDateTime.now());
	
						houseToUpdate.getPriceHistory().add(priceHistory);
	
						priceHistoryRepository.save(priceHistory);
					});
	
			houseRepository.saveAndFlush(houseToUpdate);
			return houseToUpdate;
		}
}