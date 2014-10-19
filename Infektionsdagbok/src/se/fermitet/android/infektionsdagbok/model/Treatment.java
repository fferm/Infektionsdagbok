package se.fermitet.android.infektionsdagbok.model;

import org.joda.time.DateTime;

public class Treatment {

	private String infectionType;
	private String medicine;
	private DateTime startingDate;
	private int numDays;
	
	public Treatment() {
		super();
	}
	
	public Treatment(String infectionType, String medicine, DateTime startingDate, int numDays) {
		this();
		
		this.setInfectionType(infectionType);
		this.setMedicine(medicine);
		this.setStartingDate(startingDate);
		this.setNumDays(numDays);
	}

	public String getInfectionType() {
		return this.infectionType;
	}
	
	public void setInfectionType(String infectionType) {
		this.infectionType = infectionType;
	}

	public String getMedicine() {
		return medicine;
	}

	public void setMedicine(String medicine) {
		this.medicine = medicine;
	}

	public DateTime getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(DateTime startingDate) {
		if (startingDate == null) {
			this.startingDate = null;
		} else {
			this.startingDate = startingDate.withMillisOfDay(0);
		}
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((infectionType == null) ? 0 : infectionType.hashCode());
		result = prime * result
				+ ((medicine == null) ? 0 : medicine.hashCode());
		result = prime * result + numDays;
		result = prime * result
				+ ((startingDate == null) ? 0 : startingDate.hashCode());
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
		Treatment other = (Treatment) obj;
		if (infectionType == null) {
			if (other.infectionType != null)
				return false;
		} else if (!infectionType.equals(other.infectionType))
			return false;
		if (medicine == null) {
			if (other.medicine != null)
				return false;
		} else if (!medicine.equals(other.medicine))
			return false;
		if (numDays != other.numDays)
			return false;
		if (startingDate == null) {
			if (other.startingDate != null)
				return false;
		} else if (!startingDate.equals(other.startingDate))
			return false;
		return true;
	}

	public int getNumDays() {
		return numDays;
	}

	public void setNumDays(int numDays) {
		this.numDays = numDays;
	}

	
}
