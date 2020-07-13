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
		return houseRepository.customMethod(minPrice, maxPrice);
	}

	public House findById(Long id) {
		Optional<House> obj = houseRepository.findById(id);
		return obj.orElseThrow(() -> new IllegalArgumentException("ERROR: Object not found at the database"));
	}

	@Transactional
	public List<House> insert(List<HouseDto> houseDtos) {
		return houseDtos.stream()
			.map(houseDto -> {
				House house = mapToHouse(houseDto);
				return this.insert(house, houseDto.getPrice());
			})
			.collect(Collectors.toList());
	}
	
	
	
	private House insert(House house, Long price) {
		Optional<House> optionalFoundHouse = houseRepository.findById(house.getId());
		if (optionalFoundHouse.isPresent()) {
			House houseInDb = optionalFoundHouse.get();
			
			house.setRegisteredOn(houseInDb.getRegisteredOn());
			house.setPriceHistory(houseInDb.getPriceHistory());
			
			houseInDb.getPriceHistory()
				.stream()
//				.sorted(Comparator.comparing(PriceHistory::getUpdatedOn).reversed())
				.sorted(Comparator.comparing(pricehist -> pricehist.getUpdatedOn(), Comparator.reverseOrder()))
				.findFirst()
				.filter(priceHist -> !priceHist.getPrice().equals(price))
				.ifPresent(priceHist -> {
					PriceHistory priceHistory = new PriceHistory();
					priceHistory.setHouse(house);
					priceHistory.setPrice(price);
					priceHistory.setUpdatedOn(LocalDateTime.now());
					
					house.getPriceHistory().add(priceHistory);
					
					priceHistoryRepository.save(priceHistory);	
				});
			
			houseRepository.saveAndFlush(house);
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

	
	private House mapToHouse(HouseDto dto) {
		House house = new House();
		house.setAddress(dto.getAddress());
		house.setCity(dto.getCity());
		house.setNeighborhood(dto.getNeighborhood());
		house.setId(dto.getId());

		return house;
	}
	
	
	public House update(House house) {
		House houseInDatabase = houseRepository.findById(house.getId()).orElse(null);
		if (houseInDatabase == null) {
			System.out.println("ERROR: House to update not found");
			throw new IllegalArgumentException("House to update not found");
		}
		house.setRegisteredOn(houseInDatabase.getRegisteredOn());
		return houseRepository.save(house);
	}

	public void deleteById(Long id) {
		if (!(findById(id) == null)) {
			houseRepository.deleteById(id);
		}
	}
}
