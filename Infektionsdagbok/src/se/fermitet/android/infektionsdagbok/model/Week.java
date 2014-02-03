package se.fermitet.android.infektionsdagbok.model;

import java.util.StringTokenizer;

import org.joda.time.DateTime;

public class Week {

	private static final String DELIMITER = "-";

	private DateTime dateTime;

	public Week(DateTime dateTime) {
		super();
		this.dateTime = dateTime;
	}

	public Week(String stringFromToStringOfWeek) {
		super();

		StringTokenizer tokenizer = new StringTokenizer(stringFromToStringOfWeek, DELIMITER);

		int year = Integer.valueOf(tokenizer.nextToken());
		int week = Integer.valueOf(tokenizer.nextToken());

		this.dateTime = new DateTime().withYear(year).withWeekOfWeekyear(week);
	}

	public Week previous() {
		return new Week(this.dateTime.minusWeeks(1));
	}

	public Week next() {
		return new Week(this.dateTime.plusWeeks(1));
	}


	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(dateTime.getYear()).append(DELIMITER).append(dateTime.getWeekOfWeekyear());

		return buf.toString();
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
