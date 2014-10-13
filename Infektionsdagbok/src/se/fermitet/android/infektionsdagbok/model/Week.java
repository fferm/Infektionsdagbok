package se.fermitet.android.infektionsdagbok.model;

import java.util.StringTokenizer;

import org.joda.time.DateTime;

public class Week {

	private static final String DELIMITER = "-";

	private int year;
	private int weeknum;

	public Week(DateTime dateTime) {
		super();

		this.year = dateTime.weekyear().get();
		this.weeknum = dateTime.weekOfWeekyear().get();
	}

	public Week(String stringFromToStringOfWeek) {
		super();

		StringTokenizer tokenizer = new StringTokenizer(
				stringFromToStringOfWeek, DELIMITER);

		this.year = Integer.valueOf(tokenizer.nextToken());
		this.weeknum = Integer.valueOf(tokenizer.nextToken());
	}

	public Week(int year, int weeknum) {
		this.year = year;
		this.weeknum = weeknum;
	}

	public static int weeksInTheYear(int year) {
		Week wk52 = new Week(year, 52);
		Week next = wk52.next();

		if (next.year() == year) {
			return 53;
		} else {
			return 52;
		}
	}

	private DateTime getDayInWeek(int year, int weeknum) {
		return new DateTime().withWeekyear(year).withWeekOfWeekyear(weeknum);
	}

	public Week previous() {
		return new Week(getDayInWeek(this.year(), this.weeknum()).minusWeeks(1));
	}

	public Week next() {
		return new Week(getDayInWeek(this.year(), this.weeknum()).plusWeeks(1));
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.year).append(DELIMITER).append(this.weeknum);

		return buf.toString();
	}

	public int year() {
		return year;
	}

	public int weeknum() {
		return weeknum;
	}

	public boolean isBefore(Week other) {
		if (this.year == other.year) {
			return this.weeknum < other.weeknum;
		} else {
			return this.year < other.year;
		}
	}

	public boolean isAfter(Week other) {
		if (this.year == other.year) {
			return this.weeknum > other.weeknum;
		} else {
			return this.year > other.year;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Week))
			return false;

		Week other = (Week) o;

		return (this.year() == other.year())
				&& (this.weeknum() == other.weeknum());
	}

	@Override
	public int hashCode() {
		return 21 * weeknum() + year();
	}
}
