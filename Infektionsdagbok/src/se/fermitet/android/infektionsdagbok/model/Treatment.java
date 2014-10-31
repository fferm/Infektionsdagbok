package se.fermitet.android.infektionsdagbok.model;

import org.joda.time.LocalDate;

public class Treatment extends ModelObjectBase {

	private static final long serialVersionUID = 1L;

	private LocalDate startingDate;
	private Integer numDays;
	private String infectionType;
	private String medicine;

	public Treatment() {
		super();
	}

	public Treatment(LocalDate startingDate, Integer numDays, String infectionType, String medicine) {
		this();

		this.setStartingDate(startingDate);
		this.setNumDays(numDays);
		this.setInfectionType(infectionType);
		this.setMedicine(medicine);
	}

	public Treatment(Treatment original) {
		this(original.getStartingDate(), original.getNumDays(), original.getInfectionType(), original.getMedicine());

		setUUID(original.getUUID());
	}

	public LocalDate getStartingDate() {
		return startingDate;
	}

	public String getStartingDateString() {
		return formatDate(getStartingDate());
	}

	public void setStartingDate(LocalDate startingDate) {
		this.startingDate = startingDate;
	}

	public Integer getNumDays() {
		return numDays;
	}

	public void setNumDays(Integer numDays) {
		this.numDays = numDays;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((this.infectionType == null) ? 0 : this.infectionType
						.hashCode());
		result = prime * result
				+ ((this.medicine == null) ? 0 : this.medicine.hashCode());
		result = prime * result
				+ ((this.numDays == null) ? 0 : this.numDays.hashCode());
		result = prime
				* result
				+ ((this.startingDate == null) ? 0 : this.startingDate
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Treatment other = (Treatment) obj;
		if (this.infectionType == null) {
			if (other.infectionType != null)
				return false;
		} else if (!this.infectionType.equals(other.infectionType))
			return false;
		if (this.medicine == null) {
			if (other.medicine != null)
				return false;
		} else if (!this.medicine.equals(other.medicine))
			return false;
		if (this.numDays == null) {
			if (other.numDays != null)
				return false;
		} else if (!this.numDays.equals(other.numDays))
			return false;
		if (this.startingDate == null) {
			if (other.startingDate != null)
				return false;
		} else if (!this.startingDate.equals(other.startingDate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();

		buf.append("Treatment{uuid: ").append(getUUID().toString());

		buf.append(", start: ");

		if (getStartingDate() == null) {
			buf.append("null");
		} else {
			buf.append(getStartingDateString());
		}

		buf.append(", numDays: ").append(getNumDays())
		.append(", medicine: ").append(getMedicine())
		.append(", infectionType: ").append(getInfectionType())
		.append("}");

		return buf.toString();
	}


}
