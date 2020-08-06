package com.cindyokino.houseSearch.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude="priceHistoryList")
public class House {
	
	@Id
	private Long id;
	private String address;
	private String city;
	private String neighborhood;
	private LocalDate registeredOn;
	
	@OneToMany(mappedBy = "house", orphanRemoval = true)
	private List<PriceHistoryList> priceHistoryList;
}
