package com.cindyokino.houseSearch.integrationtest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import com.cindyokino.houseSearch.entities.House;
import com.cindyokino.houseSearch.entities.PriceHistory;
import com.cindyokino.houseSearch.repositories.HouseRepository;
import com.cindyokino.houseSearch.repositories.PriceHistoryRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class NewTestDelete {

	@Autowired
	private HouseRepository houseRepository;

	@Autowired
	private PriceHistoryRepository priceHistoryRepository;

	@Test
	public void nothing() {

		
		House house = new House();
		house.setId(123L);
		house.setAddress("670 de gaspe");
		house.setCity("Montreal");
		house.setNeighborhood("Verdun");
		house.setRegisteredOn(LocalDate.now().minusDays(40L));
		
		PriceHistory priceHistory = new PriceHistory();
		priceHistory.setHouseId(123L);
		priceHistory.setPrice(133000L);
		priceHistory.setUpdatedOn(LocalDate.now().minusDays(10L));

		List<PriceHistory> priceHistories = Arrays.asList(priceHistory);
		
		house.setPriceHistory(priceHistories);
		
		houseRepository.save(house);
//		priceHistoryRepository.save(priceHistory);
		
		
		List<House> housesInDatabase = houseRepository.findAll();
		
		
		
		System.out.println();
		
		
		
		
	}
}
