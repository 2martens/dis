package de.dis2017.data;

import java.sql.Timestamp;
import java.util.Date;

public class TenancyContract extends Contract {
	private Timestamp startDate;
	private Timestamp duration;
	private int additionalCost;

	public TenancyContract(){
		super();
	}
	public TenancyContract(Contract contract){
		this.setContractNo(contract.getContractNo());
		this.setDate(contract.getDate());
		this.setPlace(contract.getPlace());
		this.setPerson(contract.getPerson());
	}
	
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
		return super.getEstate();
	}

	public void setApartment(int apartment) {
		super.setEstate(apartment);
	}
	
	
}
