package com.cindyokino.houseSearch.integrationtest;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.cindyokino.houseSearch.entities.House;
import com.cindyokino.houseSearch.repositories.HouseRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class HousesIntegrationTest {

	@Autowired
	private MockMvc mockMvc; // MockMvc simula um sistema externo que faz chamadas REST para o sistema interno

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private HouseRepository houseRepository;

	@Test
	public void findAllTest_success() throws Exception {
		House house1 = new House(1L, 150000L, "TestAddress1", "TestCity1", "TestNeighborhood1", LocalDate.now(), LocalDate.now());
		House house2 = new House(2L, 450000L, "TestAddress2", "TestCity2", "TestNeighborhood2", LocalDate.now(), LocalDate.now());
		
		List<House> expectedHouses = new ArrayList<>(Arrays.asList(house1, house2));
		
		houseRepository.saveAll(expectedHouses);

		String body = this.mockMvc.perform(get("/houses"))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		List<House> actualHouses = objectMapper.readValue(body, new TypeReference<List<House>>() {
		});

		assertThat(actualHouses, notNullValue());
		assertThat(actualHouses.size(), is(expectedHouses.size()));
		assertThat(actualHouses, Matchers.is(expectedHouses));
	}

	@Test
	public void findByIdTest_success() throws Exception {
		House expectedHouse = new House(1L, 150000L, "TestAddress1", "TestCity1", "TestNeighborhood1", LocalDate.now(), LocalDate.now());

		houseRepository.save(expectedHouse);

		String body = this.mockMvc.perform(get("/houses/1"))
				.andExpect(status().isOk())
				.andReturn().getResponse()
				.getContentAsString();

		House actualHouse = objectMapper.readValue(body, House.class);

		assertThat(actualHouse, Matchers.is(expectedHouse));
	}

	@Test
	public void findByPriceRangeTest_success() throws UnsupportedEncodingException, Exception {
		House house1 = new House(1L, 150000L, "TestAddress1", "TestCity1", "TestNeighborhood1", LocalDate.now(), LocalDate.now());
		House house2 = new House(2L, 450000L, "TestAddress2", "TestCity2", "TestNeighborhood2", LocalDate.now(), LocalDate.now());

		List<House> expectedHouses = new ArrayList<>(Arrays.asList(house1, house2));
		
		houseRepository.saveAll(expectedHouses);

		String body = this.mockMvc.perform(get("/houses?minPrice=100000&maxPrice=500000"))
				.andExpect(status().isOk())
				.andReturn().getResponse()
				.getContentAsString();

		List<House> actualHouses = objectMapper.readValue(body, new TypeReference<List<House>>() {
		});

		assertThat(actualHouses, is(expectedHouses));
	}

	@Test
	public void insertHouseTest_success() throws Exception {
		House house = new House(1L, 150000L, "TestAddress1", "TestCity1", "TestNeighborhood1", LocalDate.now(), LocalDate.now());

		List<House> expectedHouses = new ArrayList<>(Collections.singletonList(house));

		String expectedHousesJson = objectMapper.writeValueAsString(expectedHouses); 

		String body = this.mockMvc.perform(post("/houses") // perform faz a chamada REST (post no caso)
				.contentType(MediaType.APPLICATION_JSON) // parametros adicionais na chamada
				.accept(MediaType.APPLICATION_JSON)
				.content(expectedHousesJson)) // conteudo a ser enviado ao sistema
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		List<House> actualHouses = objectMapper.readValue(body, new TypeReference<List<House>>() { 
		});

		assertThat(actualHouses, is(expectedHouses));
	}

	@Test
	public void updateHouseTest_success() throws Exception {
		House emptyHouse = new House();
		emptyHouse.setId(1L);
		
		houseRepository.save(emptyHouse);
		
		House expectedHouse = new House(1L, 150000L, "TestAddress1", "TestCity1", "TestNeighborhood1", LocalDate.now(), LocalDate.now());

		String expectedHouseJson = objectMapper.writeValueAsString(expectedHouse);

		String body = this.mockMvc
				.perform(put("/houses").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(expectedHouseJson))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		House actualHouse = objectMapper.readValue(body, House.class);

		assertThat(actualHouse, is(expectedHouse));
	}

	@Test
	public void deleteHouseTest_success() throws Exception {		
		House expectedHouse = new House(1L, 150000L, "TestAddress1", "TestCity1", "TestNeighborhood1", LocalDate.now(), LocalDate.now());

		houseRepository.save(expectedHouse);
		
		this.mockMvc.perform(delete("/houses/1")).andExpect(status().isNoContent());

	}
}
