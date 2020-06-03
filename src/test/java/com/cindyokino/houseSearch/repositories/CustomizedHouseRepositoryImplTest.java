package com.cindyokino.houseSearch.repositories;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class CustomizedHouseRepositoryImplTest {

	private CustomizedHouseRepositoryImpl customizedHouseRepositoryImpl = new CustomizedHouseRepositoryImpl();
	
	
	@Test
	public void givenNoFilters_whenCreateQuery_thenReturnQueryWithoutFilters() {
		String expectedResult = "select h from House h";
		
		String actualResult = customizedHouseRepositoryImpl.createHouseQueryString(null, null);
		
		MatcherAssert.assertThat(actualResult, Matchers.is(expectedResult));
	}
	
	@Test
	public void givenMinPriceFilter_whenCreateQuery_thenReturnQueryWithMinPriceFilter() {
		String expectedResult = "select h from House h where h.price > 100000";
	
		String actualResult = customizedHouseRepositoryImpl.createHouseQueryString(100000L, null);
		
		MatcherAssert.assertThat(actualResult, Matchers.is(expectedResult));
	}
	
	@Test
	public void givenMaxPriceFilter_whenCreateQuery_thenReturnQueryWithMaxPriceFilter() {
		String expectedResult = "select h from House h where h.price < 300000";
		
		String actualResult = customizedHouseRepositoryImpl.createHouseQueryString(null, 300000L);
		
		MatcherAssert.assertThat(actualResult, Matchers.is(expectedResult));
	}
	
	@Test
	public void givenAllFilters_whenCreateQuery_thenReturnQueryWithAllFilters() {
		String expectResult = "select h from House h where h.price > 100000 and h.price < 300000";
		
		String actualResult = customizedHouseRepositoryImpl.createHouseQueryString(100000L, 300000L);
		
		MatcherAssert.assertThat(actualResult, Matchers.is(expectResult));
	}
}
