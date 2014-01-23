package se.fermitet.android.infektionsdagbok.model;

import org.joda.time.DateTime;

public class WeekAnswers {

	public Week week = new Week(new DateTime());

	public boolean generallyWell = true;
	public boolean malaise;
	public boolean fever;
	public boolean earAche;
	public boolean soreThroat;
	public boolean runnyNose;
	public boolean stommacAche;
	public boolean dryCough;
	public boolean wetCough;
	public boolean morningCough;

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof WeekAnswers)) return false;

		WeekAnswers other = (WeekAnswers) o;

		return (this.week.equals(other.week)) &&
				(this.generallyWell == other.generallyWell) &&
				(this.malaise == other.malaise) &&
				(this.fever == other.fever) &&
				(this.earAche == other.earAche) &&
				(this.soreThroat == other.soreThroat) &&
				(this.runnyNose == other.runnyNose) &&
				(this.stommacAche == other.stommacAche) &&
				(this.dryCough == other.dryCough) &&
				(this.wetCough == other.wetCough) &&
				(this.morningCough == other.morningCough);
	}

	@Override
	public int hashCode() {
		int hash = week == null ? 0 : week.hashCode();
		hash = addBooleanHash(hash, generallyWell);
		hash = addBooleanHash(hash, malaise);
		hash = addBooleanHash(hash, fever);
		hash = addBooleanHash(hash, earAche);
		hash = addBooleanHash(hash, soreThroat);
		hash = addBooleanHash(hash, runnyNose);
		hash = addBooleanHash(hash, stommacAche);
		hash = addBooleanHash(hash, dryCough);
		hash = addBooleanHash(hash, wetCough);
		hash = addBooleanHash(hash, morningCough);

		return hash;
	}

	private int addBooleanHash(int prevHash, boolean value) {
		return (37 * prevHash) + (value ? 0 : 1);
	}

}
