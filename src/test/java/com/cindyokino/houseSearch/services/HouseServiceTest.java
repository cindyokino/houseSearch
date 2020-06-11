package com.cindyokino.houseSearch.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

}
