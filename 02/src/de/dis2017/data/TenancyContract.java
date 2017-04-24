package de.dis2017.data;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.Date;

public class TenancyContract extends Contract {
	private Timestamp startDate;
	private Duration  duration;
	private int       additionalCost;

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

	public void setStartDate(Date startDate) {
		this.startDate = new Timestamp(startDate.getTime());
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}
    
    public void setDuration(Timestamp duration) {
        this.duration = Duration.ofMillis(duration.getTime());
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
