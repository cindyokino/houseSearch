package com.cindyokino.houseSearch.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.cindyokino.houseSearch.entities.House;

public class CustomizedHouseRepositoryImpl implements CustomizedHouseRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<House> customMethod(Long minPrice, Long maxPrice) {
		String findAllByRangeQueryStr = createHouseQueryString(minPrice, maxPrice);

//		TypedQuery<House> query = entityManager.createQuery(findAllByRangeQueryStr, House.class);
		TypedQuery<Long> query = entityManager.createQuery(
//				"select h from House h join "
//				""
//				+ "("
//				+ "select ph.houseId from PriceHistory ph join "
//					+ "("
//					+ "select max(priceh.id) as id from PriceHistory priceh group by priceh.house"
//					+ ") as latest_price "
//					+ "on ph.id = latest_price.id "
//					+ "where ph.price > 60 AND ph.price < 600000"
//				+ ") as price_history_info on h.id  = price_history_info.house_id"
				
//				"select ph from PriceHistory ph group by ph.house order by ph.updatedOn desc"
				
				"select max(id) from PriceHistory ph group by ph.house join fetch "
				, Long.class);
		
		query.getResultList().forEach(ph -> {
//			System.out.println(ph.getPrice());
//			System.out.println(ph.getUpdatedOn());
//			System.out.println(ph.getHouse().getId());
			
			System.out.println(ph);
		});
		return null;
	}

	public String createHouseQueryString(Long minPrice, Long maxPrice) {
		String findAllByRangeQueryStr = "select h from House h";
		if (minPrice != null || maxPrice != null) {
			findAllByRangeQueryStr = findAllByRangeQueryStr + " where";

			if (minPrice != null) {
				findAllByRangeQueryStr = findAllByRangeQueryStr + " h.price > " + minPrice;
			}

			if (maxPrice != null) {
				if (minPrice != null) {
					findAllByRangeQueryStr = findAllByRangeQueryStr + " and";
				}
				findAllByRangeQueryStr = findAllByRangeQueryStr + " h.price < " + maxPrice;
			}
		}
		return findAllByRangeQueryStr;
	}

	// select h from House h where h.price > ?1 and h.price < ?2
}
