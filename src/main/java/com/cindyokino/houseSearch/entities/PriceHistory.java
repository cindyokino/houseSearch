package com.cindyokino.houseSearch.entities;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "price_history")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude="house")
public class PriceHistory {

	@Id
	@GeneratedValue
	private Long id;
	private Long price;
	private LocalDateTime updatedOn;
	
	@ManyToOne
	@JoinColumn(name = "house_id")
	@JsonIgnore
	private House house;

}
