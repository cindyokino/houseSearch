package com.cindyokino.houseSearch.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cindyokino.houseSearch.entities.House;
import com.cindyokino.houseSearch.repositories.HouseRepository;

@ExtendWith(MockitoExtension.class)
public class HouseServiceTest {

	@InjectMocks
	private HouseService houseService;

	@Mock
	private HouseRepository houseRepositoryMock;

	@Test
	public void findAllTest_success() {
		House house1 = new House(1L, 150000L, "TestAddress1", "TestCity1", "TestNeighborhood1", LocalDate.now(),
				LocalDate.now());
		House house2 = new House(2L, 450000L, "TestAddress2", "TestCity2", "TestNeighborhood2", LocalDate.now(),
				LocalDate.now());

		List<House> expectedHouses = new ArrayList<>(Arrays.asList(house1, house2));

		Mockito.when(houseRepositoryMock.customMethod(null, null)).thenReturn(expectedHouses);

		List<House> actualHouses = houseService.findAll(null, null);

		MatcherAssert.assertThat(actualHouses, Matchers.is(expectedHouses));
	}

	@Test
	public void findByIdTest_success() {
		Optional<House> expectedHouse = Optional.of(new House(1L, 150000L, "TestAddress1", "TestCity1", "TestNeighborhood1", LocalDate.now(),
				LocalDate.now()));
		
		Mockito.when(houseRepositoryMock.findById(1L)).thenReturn(expectedHouse);
		
		House actualHouse = houseService.findById(1L);
		
		MatcherAssert.assertThat(actualHouse, Matchers.is(expectedHouse.get()));		
	}
	
	@Test
	public void insertHouse_forExistingId_keepRegisteredOn() {
		House house = new House(1L, 150000L, "TestAddress1", "TestCity1", "TestNeighborhood1", LocalDate.now().minusDays(1), LocalDate.now().minusDays(1));
		List<House> inputList = new ArrayList<>();		
		inputList.add(house);
		
		Optional<House> expectedHouse = Optional.of(house);
				
		Mockito.when(houseRepositoryMock.findById(house.getId())).thenReturn(expectedHouse);
		Mockito.when(houseRepositoryMock.saveAll(inputList)).thenReturn(inputList);
			
		List<House> actualHouseList = houseService.insert(inputList);
		
		MatcherAssert.assertThat(actualHouseList.size(), Matchers.is(1));	
		MatcherAssert.assertThat(actualHouseList.get(0).getRegisteredOn(), Matchers.is(house.getRegisteredOn()));	
		MatcherAssert.assertThat(actualHouseList.get(0).getUpdatedOn(), Matchers.is(LocalDate.now()));	
	}
	
	@Test
	public void insertHouse_forNewId() {
		House house = new House(1L, 150000L, "TestAddress1", "TestCity1", "TestNeighborhood1", LocalDate.now(), LocalDate.now());

		List<House> inputList = new ArrayList<>(Arrays.asList(house));
		
		Mockito.when(houseRepositoryMock.findById(house.getId())).thenReturn(Optional.empty());
		Mockito.when(houseRepositoryMock.saveAll(inputList)).thenReturn(inputList);
		
		List<House> actualHouseList = houseService.insert(inputList);
		
		MatcherAssert.assertThat(actualHouseList.get(0).getRegisteredOn(), Matchers.is(house.getRegisteredOn()));
		MatcherAssert.assertThat(actualHouseList.get(0).getUpdatedOn(), Matchers.is(house.getUpdatedOn()));
	}
	

}







