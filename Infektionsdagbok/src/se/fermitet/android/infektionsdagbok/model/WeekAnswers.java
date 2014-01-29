package se.fermitet.android.infektionsdagbok.model;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.R;

public class WeekAnswers {

	public Week week = new Week(new DateTime());

	private boolean generallyWell = true;
	private boolean malaise;
	private boolean fever;
	private boolean earAche;
	private boolean soreThroat;
	private boolean runnyNose;
	private boolean stommacAche;
	private boolean dryCough;
	private boolean wetCough;
	private boolean morningCough;

	public boolean getAnswer(int id) {
		if (id == R.id.generallyWell) {
			return generallyWell;
		} else if (id == R.id.malaise) {
			return malaise;
		} else if (id == R.id.fever) {
			return fever;
		} else if (id == R.id.earAche) {
			return earAche;
		} else if (id == R.id.soreThroat) {
			return soreThroat;
		} else if (id == R.id.runnyNose) {
			return runnyNose;
		} else if (id == R.id.stommacAche) {
			return stommacAche;
		} else if (id == R.id.dryCough) {
			return dryCough;
		} else if (id == R.id.wetCough) {
			return wetCough;
		} else if (id == R.id.morningCough) {
			return morningCough;
		}
		return false;
	}

	public void setAnswer(int id, boolean value) {
		if (id == R.id.generallyWell) {
			this.generallyWell = value;
		} else if (id == R.id.malaise) {
			this.malaise = value;
		} else if (id == R.id.fever) {
			this.fever = value;
		} else if (id == R.id.earAche) {
			this.earAche = value;
		} else if (id == R.id.soreThroat) {
			this.soreThroat = value;
		} else if (id == R.id.runnyNose) {
			this.runnyNose = value;
		} else if (id == R.id.stommacAche) {
			this.stommacAche = value;
		} else if (id == R.id.dryCough) {
			this.dryCough = value;
		} else if (id == R.id.wetCough) {
			this.wetCough = value;
		} else if (id == R.id.morningCough) {
			this.morningCough = value;
		}
	}




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
