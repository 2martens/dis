package de.dis2017.data;

public class PurchaseContract extends Contract {
	private int noOfInstallments;
	private int interestRate;
	
	private int house;

	public PurchaseContract(){
		super();
	}
	public PurchaseContract(Contract contract){
		this.setContractNo(contract.getContractNo());
		this.setDate(contract.getDate());
		this.setPlace(contract.getPlace());
		this.setPerson(contract.getPerson());
	}
	
	public int getNoOfInstallments() {
		return noOfInstallments;
	}

	public void setNoOfInstallments(int noOfInstallments) {
		this.noOfInstallments = noOfInstallments;
	}

	public int getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(int interestRate) {
		this.interestRate = interestRate;
	}

	public int getHouse() {
		return house;
	}

	public void setHouse(int house) {
		this.house = house;
	}
	
	
}
