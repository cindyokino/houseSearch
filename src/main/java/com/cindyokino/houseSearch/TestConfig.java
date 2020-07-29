package com.cindyokino.houseSearch;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.cindyokino.houseSearch.entities.House;
import com.cindyokino.houseSearch.entities.PriceHistory;
import com.cindyokino.houseSearch.repositories.HouseRepository;
import com.cindyokino.houseSearch.repositories.PriceHistoryRepository;

@Profile("dev")
@Configuration
public class TestConfig implements CommandLineRunner { // CommandLineRunner inicializa um codigo junto com a inicializacao do programa
	
	@Autowired
	private HouseRepository houseRepository;
	
	@Autowired
	private PriceHistoryRepository priceHistoryRepository;
	

	@Override
	public void run(String... args) throws Exception {

		House house1 = new House(14158660L, "851, Rue Jean-Béliveau", "Longueuil", "Quartier Centre", LocalDate.now().minusDays(4), null);
		House house2 = new House(13262772L, "2000, Boulevard Queen", "Saint-Lambert", "New Neighborhood", LocalDate.now().minusDays(7), null);
		
		houseRepository.save(house1);
		houseRepository.save(house2);
		
		
		PriceHistory priceHistory1 = PriceHistory.builder()
				.house(house1)
				.price(500000L)
				.updatedOn(LocalDateTime.now().minusDays(2))
				.build();
		
		PriceHistory priceHistory2 = PriceHistory.builder()
				.house(house2)
				.price(800000L)
				.updatedOn(LocalDateTime.now().minusDays(1))
				.build();
		
		PriceHistory priceHistory3 = PriceHistory.builder()
				.house(house2)
				.price(900000L)
				.updatedOn(LocalDateTime.now().minusDays(5))
				.build();
		
		priceHistoryRepository.saveAll(Arrays.asList(priceHistory1, priceHistory2, priceHistory3));
	
	}
}
