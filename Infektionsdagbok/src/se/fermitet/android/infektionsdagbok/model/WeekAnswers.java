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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WeekAnswers other = (WeekAnswers) obj;
		if (this.dryCough != other.dryCough)
			return false;
		if (this.earAche != other.earAche)
			return false;
		if (this.fever != other.fever)
			return false;
		if (this.generallyWell != other.generallyWell)
			return false;
		if (this.malaise != other.malaise)
			return false;
		if (this.morningCough != other.morningCough)
			return false;
		if (this.runnyNose != other.runnyNose)
			return false;
		if (this.soreThroat != other.soreThroat)
			return false;
		if (this.stommacAche != other.stommacAche)
			return false;
		if (this.week == null) {
			if (other.week != null)
				return false;
		} else if (!this.week.equals(other.week))
			return false;
		if (this.wetCough != other.wetCough)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.dryCough ? 1231 : 1237);
		result = prime * result + (this.earAche ? 1231 : 1237);
		result = prime * result + (this.fever ? 1231 : 1237);
		result = prime * result + (this.generallyWell ? 1231 : 1237);
		result = prime * result + (this.malaise ? 1231 : 1237);
		result = prime * result + (this.morningCough ? 1231 : 1237);
		result = prime * result + (this.runnyNose ? 1231 : 1237);
		result = prime * result + (this.soreThroat ? 1231 : 1237);
		result = prime * result + (this.stommacAche ? 1231 : 1237);
		result = prime * result
				+ ((this.week == null) ? 0 : this.week.hashCode());
		result = prime * result + (this.wetCough ? 1231 : 1237);
		return result;
	}

}
