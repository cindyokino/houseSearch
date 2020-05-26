package com.cindyokino.houseSearch.config;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.cindyokino.houseSearch.entities.House;
import com.cindyokino.houseSearch.repositories.HouseRepository;

@Configuration
public class TestConfig implements CommandLineRunner { // CommandLineRunner inicializa um codigo junto com a inicializacao do programa

	@Autowired
	private HouseRepository houseRepository;

	@Override
	public void run(String... args) throws Exception {

		House house1 = new House(14158660L, 200_000d, "2000, Rue Jean-Béliveau", "Longueuil (Le Vieux-Longueuil)", "Quartier Centre", LocalDate.now(), LocalDate.now());
		House house2 = new House(13262772L, 1_120_000d, "864, boulevard Queen", "Saint-Lambert (Montérégie)", null, LocalDate.now(), LocalDate.now());

		houseRepository.save(house1);
		houseRepository.save(house2);
	}

}