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
	public List<House> customMethod(Double minPrice, Double maxPrice) {
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

		TypedQuery<House> query = entityManager.createQuery(findAllByRangeQueryStr, House.class);

		return query.getResultList();
	}

	// select h from House h where h.price > ?1 and h.price < ?2
}
