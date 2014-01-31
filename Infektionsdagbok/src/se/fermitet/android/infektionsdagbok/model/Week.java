package se.fermitet.android.infektionsdagbok.model;

import org.joda.time.DateTime;

public class Week {

	private DateTime dateTime;

	public Week(DateTime dateTime) {
		this.dateTime = dateTime;
	}

	public Week previous() {
		return new Week(this.dateTime.minusWeeks(1));
	}

	public Week next() {
		return new Week(this.dateTime.plusWeeks(1));
	}


	@Override
	public String toString() {
		return "" + dateTime.getYear() + "-" + dateTime.getWeekOfWeekyear();
	}

	private int year() {
		return dateTime.getYear();
	}

	private int weeknum() {
		return dateTime.getWeekOfWeekyear();
	}


	@Override
	public boolean equals(Object o) {
		if (! (o instanceof Week)) return false;

		Week other = (Week) o;

		return (this.year() == other.year()) && (this.weeknum() == other.weeknum());
	}

	@Override
	public int hashCode() {
		return 21 * weeknum() + year();
	}

}
