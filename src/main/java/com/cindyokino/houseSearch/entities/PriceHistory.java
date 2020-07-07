package com.cindyokino.houseSearch.entities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "price_history")
public class PriceHistory {

	@Id
	@GeneratedValue
	private Long id;
	private Long houseId;
	private LocalDate updatedOn;
	private Long price;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getHouseId() {
		return houseId;
	}

	public void setHouseId(Long houseId) {
		this.houseId = houseId;
	}

	public LocalDate getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(LocalDate updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

}
