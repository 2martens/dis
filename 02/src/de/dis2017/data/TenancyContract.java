package de.dis2017.data;

import java.sql.Timestamp;

public class TenancyContract extends Contract {
	private Timestamp startDate;
	private Timestamp duration;
	private int       additionalCost;
	
	private int apartment;

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Timestamp getDuration() {
		return duration;
	}

	public void setDuration(Timestamp duration) {
		this.duration = duration;
	}

	public int getAdditionalCost() {
		return additionalCost;
	}

	public void setAdditionalCost(int additionalCost) {
		this.additionalCost = additionalCost;
	}

	public int getApartment() {
		return apartment;
	}

	public void setApartment(int apartment) {
		this.apartment = apartment;
	}
}
