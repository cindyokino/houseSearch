package com.cindyokino.houseSearch.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cindyokino.houseSearch.entities.PriceHistory;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {

}
