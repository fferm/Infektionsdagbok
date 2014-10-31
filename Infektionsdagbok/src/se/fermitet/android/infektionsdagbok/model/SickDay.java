package se.fermitet.android.infektionsdagbok.model;

import org.joda.time.LocalDate;

public class SickDay extends ModelObjectBase {

	private static final long serialVersionUID = 1L;
	private LocalDate start;
	private LocalDate end;

	public SickDay() {
		super();
	}

	public SickDay(LocalDate start, LocalDate end) {
		this();

		this.setStart(start);
		this.setEnd(end);
	}

	public SickDay(SickDay original) {
		this(original.getStart(), original.getEnd());

		this.uuid = original.getUUID();
	}

	public LocalDate getStart() {
		return this.start;
	}

	public String getStartString() {
		return formatDate(getStart());
	}

	public void setStart(LocalDate start) {
		this.start = start;
	}

	public LocalDate getEnd() {
		return this.end;
	}

	public String getEndString() {
		return formatDate(getEnd());
	}

	public void setEnd(LocalDate end) {
		this.end = end;
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

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();

		buf.append("SickDay{uuid: ").append(getUUID().toString());

		buf.append(", start: ");
		if (getStart() == null) {
			buf.append("null");
		} else {
			buf.append(getStartString());
		}

		buf.append(", end: ");
		if (getEnd() == null) {
			buf.append("null");
		} else {
			buf.append(getEndString());
		}

		buf.append("}");

		return buf.toString();
	}

}
