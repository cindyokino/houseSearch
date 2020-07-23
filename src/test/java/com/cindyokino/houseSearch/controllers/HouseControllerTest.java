package com.cindyokino.houseSearch.controllers;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.cindyokino.houseSearch.entities.House;
import com.cindyokino.houseSearch.entities.HouseDto;
import com.cindyokino.houseSearch.services.HouseService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(HouseController.class)
public class HouseControllerTest {

	@Autowired
	private MockMvc mockMvc; // MockMvc simula um sistema EXTERNO que faz chamadas REST para o sistema INTERNO

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private HouseService houseServiceMock;

	@Test
	public void findAllTest_success() throws Exception {
		House house1 = new House(1L, "TestAddress1", "TestCity1", "TestNeighborhood1", LocalDate.now(), null);
		House house2 = new House(2L, "TestAddress2", "TestCity2", "TestNeighborhood2", LocalDate.now(), null);

		List<House> expectedHouses = new ArrayList<>(Arrays.asList(house1, house2));

		when(houseServiceMock.findAll(any(), any())).thenReturn(expectedHouses);

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
		House expectedHouse = new House(1L, "TestAddress1", "TestCity1", "TestNeighborhood1", LocalDate.now(), null);

		when(houseServiceMock.findById(eq(1L))).thenReturn(expectedHouse);

		String body = this.mockMvc.perform(get("/houses/1"))
				.andExpect(status().isOk())
				.andReturn().getResponse()
				.getContentAsString();

		House actualHouse = objectMapper.readValue(body, House.class);

		assertThat(actualHouse, Matchers.is(expectedHouse));
	}

	@Test
	public void findByPriceRangeTest_success() throws UnsupportedEncodingException, Exception {
		House house1 = new House(1L, "TestAddress1", "TestCity1", "TestNeighborhood1", LocalDate.now(), null);
		House house2 = new House(2L, "TestAddress2", "TestCity2", "TestNeighborhood2", LocalDate.now(), null);

		List<House> expectedHouses = new ArrayList<>(Arrays.asList(house1, house2));

		when(houseServiceMock.findAll(100000L, 500000L)).thenReturn(expectedHouses);

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
		HouseDto houseDto = new HouseDto(1L, "TestAddress1", "TestCity1", "TestNeighborhood1", 409000L);
		House newHouse = mapToHouse(houseDto);
		
		List<HouseDto> housesDtoList = new ArrayList<>(Collections.singletonList(houseDto));
		List<House> expectedHouses = new ArrayList<>(Collections.singletonList(newHouse));

		/* writeValueAsString transforma um objeto java em uma string json para a comunicacao do sistema
		 externo(mockMvc, onde faco chamadas REST) e interno */
		String expectedHousesJson = objectMapper.writeValueAsString(housesDtoList); 

		// when(chamada_de_metodo).thenReturn(valor_de_retorno)
		when(houseServiceMock.insert(Mockito.any())).thenReturn(expectedHouses);

		String body = this.mockMvc.perform(post("/houses") // perform faz a chamada REST (post no caso)
				.contentType(MediaType.APPLICATION_JSON) // parametros adicionais na chamada
				.accept(MediaType.APPLICATION_JSON)
				.content(expectedHousesJson)) // conteudo a ser enviado ao sistema
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		// readValue transforma string Json em objeto java
		List<House> actualHouses = objectMapper.readValue(body, new TypeReference<List<House>>() { 
		});

		assertThat(actualHouses, is(expectedHouses));
	}

	@Test
	public void updateHouseTest_success() throws Exception {
		HouseDto houseDto = new HouseDto(1L, "TestAddress1", "TestCity1", "TestNeighborhood1", 409000L);
		House houseToUpdate = mapToHouse(houseDto);

		String expectedHouseJson = objectMapper.writeValueAsString(houseToUpdate);

		when(houseServiceMock.update(Mockito.any())).thenReturn(houseToUpdate);

		String body = this.mockMvc
				.perform(put("/houses").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(expectedHouseJson))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		House actualHouse = objectMapper.readValue(body, House.class);

		assertThat(actualHouse, is(houseToUpdate));
	}
	
	@Test
	public void deleteHouseTest_success() throws Exception {

		this.mockMvc.perform(delete("/houses/1")).andExpect(status().isNoContent());

		verify(houseServiceMock).deleteById(eq(1L));
	}
	
	private House mapToHouse(HouseDto dto) {
		House house = new House();
		house.setAddress(dto.getAddress());
		house.setCity(dto.getCity());
		house.setNeighborhood(dto.getNeighborhood());
		house.setId(dto.getId());
		
		return house;
	}
}
