package com.cindyokino.houseSearch.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cindyokino.houseSearch.entities.House;

public interface HouseRepository extends JpaRepository<House, Long> {
	
	@Query(value = "select x.house_id as id, updated_on, address, city, neighborhood, registered_on from price_history\n" + 
			"  join (select house_id, max(updated_on) as upd from price_history group by house_id) as x \n" + 
			"  on price_history.house_id = x.house_id and price_history.updated_on = x.upd\n" + 
			"  join house on price_history.house_id = house.id\n" + 
			"  where (:minPrice is null OR price >= :minPrice)\n" +
			"  and (:maxPrice is null OR price <= :maxPrice)"
			,
			nativeQuery = true)
	List<House> findHousesByPriceRange(Long minPrice, Long maxPrice);

}
