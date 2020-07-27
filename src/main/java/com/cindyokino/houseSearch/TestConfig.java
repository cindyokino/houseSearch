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

		House house1 = new House(14158660L, "2000, Rue Jean-Béliveau", "Longueuil (Le Vieux-Longueuil)", "Quartier Centre", LocalDate.now(), null);
		House house2 = new House(13262772L, "864, boulevard Queen", "Saint-Lambert (Montérégie)", "New Neighborhood", LocalDate.now(), null);
		
		houseRepository.save(house1);
		houseRepository.save(house2);
		
		
		PriceHistory priceHistory1 = PriceHistory.builder()
				.house(house1)
				.price(50L)
				.updatedOn(LocalDateTime.now().minusDays(4))
				.build();
		
		PriceHistory priceHistory2 = PriceHistory.builder()
				.house(house2)
				.price(80L)
				.updatedOn(LocalDateTime.now().minusDays(7))
				.build();
		
		PriceHistory priceHistory3 = PriceHistory.builder()
				.house(house2)
				.price(90L)
				.updatedOn(LocalDateTime.now().minusDays(5))
				.build();
		
		priceHistoryRepository.saveAll(Arrays.asList(priceHistory1, priceHistory2, priceHistory3));
	
	}
}
