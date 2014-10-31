package se.fermitet.android.infektionsdagbok.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.UUID;

import org.joda.time.LocalDate;

public abstract class ModelObjectBase implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static DateFormat df;
	protected UUID uuid;

	static {
		df = DateFormat.getDateInstance(DateFormat.SHORT);
	}


	public ModelObjectBase() {
		super();

		this.uuid = UUID.randomUUID();
	}

	public UUID getUUID() {
		return this.uuid;
	}

	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}

	protected String formatDate(LocalDate date) {
		if (date == null) return null;
		else return df.format(date.toDate());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.uuid == null) ? 0 : this.uuid.hashCode());
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
		ModelObjectBase other = (ModelObjectBase) obj;
		if (this.uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!this.uuid.equals(other.uuid))
			return false;
		return true;
	}



}
