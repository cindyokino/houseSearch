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

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hamcrest.CoreMatchers;
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
import com.cindyokino.houseSearch.entities.HouseDto;
import com.cindyokino.houseSearch.repositories.HouseRepository;
import com.cindyokino.houseSearch.services.HouseService;
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
	
	@Autowired
	private HouseService houseService;
	
	@Autowired
	private EntityManager entityManager;
	
	@Test
	public void findAllTest_success() throws Exception {
		HouseDto houseDto1 = new HouseDto(1L, "TestAddress1", "TestCity1", "TestNeighborhood1", null);
		HouseDto houseDto2 = new HouseDto(2L, "TestAddress2", "TestCity2", "TestNeighborhood2", null);
				
		List<HouseDto> houseDtos = new ArrayList<>(Arrays.asList(houseDto1, houseDto2));
		
		List<House> savedHouses = houseService.insert(houseDtos);

		String body = this.mockMvc.perform(get("/houses"))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		List<House> actualHouses = objectMapper.readValue(body, new TypeReference<List<House>>() {
		});

		assertThat(actualHouses, notNullValue());
		assertThat(actualHouses.size(), is(savedHouses.size()));
		assertThat(actualHouses, Matchers.is(savedHouses));
	}

	@Test
	public void findByIdTest_success() throws Exception {
		House expectedHouse = new House(1L, "TestAddress1", "TestCity1", "TestNeighborhood1", LocalDate.now(), null);

		houseRepository.save(expectedHouse);

		String body = this.mockMvc.perform(get("/houses/1"))
				.andExpect(status().isOk())
				.andReturn().getResponse()
				.getContentAsString();

		House actualHouse = objectMapper.readValue(body, House.class);

		assertThat(actualHouse, Matchers.is(expectedHouse));
	}

	public void findByPriceRangeTest_success() throws UnsupportedEncodingException, Exception {
		House house1 = new House(1L, "TestAddress1", "TestCity1", "TestNeighborhood1", LocalDate.now(), null);
		House house2 = new House(2L, "TestAddress2", "TestCity2", "TestNeighborhood2", LocalDate.now(), null);

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
		House house = new House(1L, "TestAddress1", "TestCity1", "TestNeighborhood1", LocalDate.now(), null);

		List<House> expectedHouses = new ArrayList<>(Collections.singletonList(house));

		String expectedHousesJson = objectMapper.writeValueAsString(expectedHouses); 

		String body = this.mockMvc.perform(post("/houses") // perform faz a chamada REST (post no caso)
				.contentType(MediaType.APPLICATION_JSON) // parametros adicionais na chamada
				.accept(MediaType.APPLICATION_JSON)
				.content(expectedHousesJson)) // conteudo a ser enviado ao sistema
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		List<House> actualHouses = objectMapper.readValue(body, new TypeReference<List<House>>() { 
		});

		assertThat(actualHouses, Matchers.is(expectedHouses));
	}

	@Test
	public void updateHouseTest_success() throws Exception {		
		HouseDto houseToUpdate = new HouseDto(1L, "TestAddress1", "TestCity1", "TestNeighborhood1", 500000L);
		houseService.insert(Collections.singletonList(houseToUpdate));

		houseToUpdate.setPrice(600000L);
		houseToUpdate.setCity("New City");
		houseToUpdate.setNeighborhood("New Neighborhood");
		houseToUpdate.setAddress("New Address");
		String expectedHouseJson = objectMapper.writeValueAsString(houseToUpdate);

		entityManager.flush();
		entityManager.clear();
		
		String body = this.mockMvc.perform(put("/houses")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(expectedHouseJson))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		House actualHouse = objectMapper.readValue(body, House.class);

		assertThat(actualHouse, CoreMatchers.notNullValue());
		assertThat(actualHouse.getCity(), is(houseToUpdate.getCity()));
		assertThat(actualHouse.getNeighborhood(), is(houseToUpdate.getNeighborhood()));
		assertThat(actualHouse.getAddress(), is(houseToUpdate.getAddress()));
		assertThat(actualHouse.getPriceHistoryList(), Matchers.hasSize(2));
		
	}
	
	@Test
	public void updateHouseTest_houseNotFound() throws Exception {
		HouseDto houseToUpdate = new HouseDto(1L, "TestAddress1", "TestCity1", "TestNeighborhood1", 500000L);
		
		String expectedHouseJson = objectMapper.writeValueAsString(houseToUpdate);
		
		this.mockMvc.perform(put("/houses")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(expectedHouseJson))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void deleteHouseTest_success() throws Exception {		
		House expectedHouse = new House(1L, "TestAddress1", "TestCity1", "TestNeighborhood1", LocalDate.now(), null);

		houseRepository.save(expectedHouse);
		
		this.mockMvc.perform(delete("/houses/1")).andExpect(status().isNoContent());

	}
}