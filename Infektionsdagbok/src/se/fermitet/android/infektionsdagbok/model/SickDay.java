package se.fermitet.android.infektionsdagbok.model;

import org.joda.time.DateTime;

public class SickDay extends ModelObjectBase {

	private static final long serialVersionUID = 1L;
	private DateTime start;
	private DateTime end;

	public SickDay() {
		super();
	}

	public SickDay(DateTime start, DateTime end) {
		this();

		this.setStart(start);
		this.setEnd(end);
	}

	public SickDay(SickDay original) {
		this(original.getStart(), original.getEnd());

		this.uuid = original.getUUID();
	}

	public DateTime getStart() {
		return this.start;
	}

	public void setStart(DateTime start) {
		if (start == null) {
			this.start = null;
		} else {
			this.start = start.withMillisOfDay(0);
		}
	}

	public DateTime getEnd() {
		return this.end;
	}

	public void setEnd(DateTime end) {
		if (end == null) {
			this.end = null;
		} else {
			this.end = end.withMillisOfDay(0);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((this.end == null) ? 0 : this.end.hashCode());
		result = prime * result
				+ ((this.start == null) ? 0 : this.start.hashCode());
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
		SickDay other = (SickDay) obj;
		if (this.end == null) {
			if (other.end != null)
				return false;
		} else if (!this.end.equals(other.end))
			return false;
		if (this.start == null) {
			if (other.start != null)
				return false;
		} else if (!this.start.equals(other.start))
			return false;
		return true;
	}



}
