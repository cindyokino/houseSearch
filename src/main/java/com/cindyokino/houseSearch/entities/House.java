package com.cindyokino.houseSearch.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class House {
	
	@Id
	private Long id;
	private String address;
	private String city;
	private String neighborhood;
	private LocalDate registeredOn;
	
	@OneToMany(cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinColumn(name = "houseId")
	private List<PriceHistory> priceHistory;
	
	public House() {		
	}

	public House(Long id, String address, String city, String neighborhood, LocalDate registeredOn) {
		this.id = id;
		this.address = address;
		this.city = city;
		this.neighborhood = neighborhood;
		this.registeredOn = registeredOn;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public LocalDate getRegisteredOn() {
		return registeredOn;
	}

	public void setRegisteredOn(LocalDate registeredOn) {
		this.registeredOn = registeredOn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		House other = (House) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public List<PriceHistory> getPriceHistory() {
		return priceHistory;
	}

	public void setPriceHistory(List<PriceHistory> priceHistory) {
		this.priceHistory = priceHistory;
	}
}
