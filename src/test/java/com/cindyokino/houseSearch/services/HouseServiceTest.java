package com.cindyokino.houseSearch.services;

import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cindyokino.houseSearch.entities.House;
import com.cindyokino.houseSearch.entities.HouseDto;
import com.cindyokino.houseSearch.entities.PriceHistory;
import com.cindyokino.houseSearch.repositories.HouseRepository;
import com.cindyokino.houseSearch.repositories.PriceHistoryRepository;

@ExtendWith(MockitoExtension.class)
public class HouseServiceTest {

	@InjectMocks
	private HouseService houseService;

	@Mock
	private HouseRepository houseRepositoryMock;
	
	@Mock
	private PriceHistoryRepository priceHistoryRepositoryMock;

	@Test
	public void findAllTest_success() {
		House house1 = new House(1L, "TestAddress1", "TestCity1", "TestNeighborhood1", LocalDate.now(), null);
		House house2 = new House(2L, "TestAddress2", "TestCity2", "TestNeighborhood2", LocalDate.now(), null);

		List<House> expectedHouses = new ArrayList<>(Arrays.asList(house1, house2));

		Mockito.when(houseRepositoryMock.customMethod(null, null)).thenReturn(expectedHouses);

		List<House> actualHouses = houseService.findAll(null, null);

		MatcherAssert.assertThat(actualHouses, Matchers.is(expectedHouses));
	}

	@Test
	public void findByIdTest_success() {
		Optional<House> expectedHouse = Optional.of(new House(1L, "TestAddress1", "TestCity1", "TestNeighborhood1", LocalDate.now(), null));
		
		Mockito.when(houseRepositoryMock.findById(1L)).thenReturn(expectedHouse);
		
		House actualHouse = houseService.findById(1L);
		
		MatcherAssert.assertThat(actualHouse, Matchers.is(expectedHouse.get()));		
	}	
	
	@Test
	public void insertHouseTest_forExistingId_keepRegisteredOn() {
		HouseDto houseDto = new HouseDto(1L, "TestAddress1", "TestCity1", "TestNeighborhood1", 500000L);
		House newHouse = mapToHouse(houseDto);
		PriceHistory priceHistory = new PriceHistory();
		priceHistory.setPrice(500001L);
		House existingHouse = 
				new House(1L, "Old Address", "Old City", "Old neighborhood", LocalDate.now().minusDays(5L), new ArrayList<>(Collections.singletonList(priceHistory)));
			
		Mockito.when(houseRepositoryMock.findById(houseDto.getId())).thenReturn(Optional.of(existingHouse));
		Mockito.when(houseRepositoryMock.saveAndFlush(Mockito.any())).thenReturn(newHouse);
		Mockito.when(priceHistoryRepositoryMock.save(Mockito.any())).thenReturn(priceHistory);
			
		List<HouseDto> expectedHouse = Arrays.asList(houseDto);		
		List<House> actualHouseList = houseService.insert(expectedHouse);
		
		MatcherAssert.assertThat(actualHouseList.size(), Matchers.is(1));	
		MatcherAssert.assertThat(actualHouseList.get(0).getRegisteredOn(), Matchers.is(existingHouse.getRegisteredOn()));	
		MatcherAssert.assertThat(actualHouseList.get(0).getPriceHistory().size(), Matchers.is(2));
		MatcherAssert.assertThat(actualHouseList.get(0).getPriceHistory().get(0).getPrice(), Matchers.is(priceHistory.getPrice()));
	}
	
	@Test
	public void insertHouseTest_forNewId() {
		HouseDto houseDto = new HouseDto(1L, "TestAddress1", "TestCity1", "TestNeighborhood1", 500000L);
		House newHouse = mapToHouse(houseDto);
		PriceHistory priceHistory = new PriceHistory();
							
		Mockito.when(houseRepositoryMock.findById(newHouse.getId())).thenReturn(Optional.empty());
		Mockito.when(houseRepositoryMock.saveAndFlush(Mockito.any())).thenReturn(newHouse);
		Mockito.when(priceHistoryRepositoryMock.save(Mockito.any())).thenReturn(priceHistory);
		
		List<HouseDto> expectedHouse = Arrays.asList(houseDto);		
		List<House> actualHouseList = houseService.insert(expectedHouse);
		
		MatcherAssert.assertThat(actualHouseList.size(), Matchers.is(1));
		MatcherAssert.assertThat(actualHouseList.get(0).getRegisteredOn(), Matchers.is(LocalDate.now()));
		MatcherAssert.assertThat(actualHouseList.get(0).getPriceHistory().size(), Matchers.is(1));
		MatcherAssert.assertThat(actualHouseList.get(0).getPriceHistory().get(0).getPrice(), Matchers.is(houseDto.getPrice()));
	}
	
	@Test
	public void updateHouseTest_byId() {
		HouseDto houseDto = new HouseDto(1L, "TestAddress1", "TestCity1", "TestNeighborhood1", 100000L);
		PriceHistory priceHistory = new PriceHistory();
		priceHistory.setPrice(100100L);
		House existingHouse = 
				new House(1L, "Old Address", "Old City", "Old neighborhood", LocalDate.now().minusDays(5L), new ArrayList<>(Collections.singletonList(priceHistory)));
					
		Mockito.when(houseRepositoryMock.findById(houseDto.getId())).thenReturn(Optional.of(existingHouse));
		Mockito.when(priceHistoryRepositoryMock.save(Mockito.any())).thenReturn(priceHistory);

		House actualHouse = houseService.update(houseDto);
		
		MatcherAssert.assertThat(actualHouse.getRegisteredOn(), Matchers.is(existingHouse.getRegisteredOn()));
	}
	
	@Test
	public void updateHouseTest_forIdNull_checkForException() {
		HouseDto houseToUpdate = new HouseDto(1L, "TestAddress1", "TestCity1", "TestNeighborhood1", null);

		Mockito.when(houseRepositoryMock.findById(houseToUpdate.getId())).thenReturn(Optional.empty());
			
		Assertions.assertThrows(IllegalArgumentException.class, () -> { // checar se houve uma excecao
			houseService.update(houseToUpdate);
		});
	}
	
	@Test
	public void deleteHouseTest_byId() {
		House house = new House(1L, "TestAddress1", "TestCity1", "TestNeighborhood1", LocalDate.now().minusDays(1), null);
			
		Optional<House> expectedHouse = Optional.of(house);
		
		Mockito.when(houseRepositoryMock.findById(1L)).thenReturn(expectedHouse);
				
		houseService.deleteById(1L);
		
		verify(houseRepositoryMock).deleteById(1L);
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