package se.fermitet.android.infektionsdagbok.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.UUID;

import org.joda.time.DateTime;

public class Treatment implements Serializable {

	private static final long serialVersionUID = 1L;

	private DateTime startingDate;
	private Integer numDays;
	private String infectionType;
	private String medicine;
	private static DateFormat df;

	private UUID uuid;

	static {
		df = DateFormat.getDateInstance(DateFormat.SHORT);
	}

	public Treatment() {
		super();

		this.uuid = UUID.randomUUID();
	}

	public Treatment(DateTime startingDate, Integer numDays, String infectionType, String medicine) {
		this();

		this.setStartingDate(startingDate);
		this.setNumDays(numDays);
		this.setInfectionType(infectionType);
		this.setMedicine(medicine);
	}

	public Treatment(Treatment original) {
		this(original.getStartingDate(), original.getNumDays(), original.getInfectionType(), original.getMedicine());

		this.uuid = original.uuid;
	}

	public UUID getUUID() {
		return this.uuid;
	}

	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}

	public DateTime getStartingDate() {
		return startingDate;
	}

	public String getStartingDateString() {
		DateTime date = getStartingDate();

		if (date == null) {
			return null;
		} else {
			return df.format(date.toDate());
		}
	}

	public void setStartingDate(DateTime startingDate) {
		if (startingDate == null) {
			this.startingDate = null;
		} else {
			this.startingDate = startingDate.withMillisOfDay(0);
		}
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
		int result = 1;
		result = prime * result
				+ ((infectionType == null) ? 0 : infectionType.hashCode());
		result = prime * result
				+ ((medicine == null) ? 0 : medicine.hashCode());
		result = prime * result + numDays;
		result = prime * result
				+ ((startingDate == null) ? 0 : startingDate.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
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
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
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
