package se.fermitet.android.infektionsdagbok.model;

import java.util.ArrayList;
import java.util.List;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.helper.NameFromIdHelper;
import android.util.SparseBooleanArray;

public class WeekAnswers {

	public static List<Integer> questionIds;

	public Week week;

	private SparseBooleanArray answers;

	static {
		questionIds = new ArrayList<Integer>();

		questionIds.add(R.id.generallyWell);
		questionIds.add(R.id.malaise);
		questionIds.add(R.id.fever);
		questionIds.add(R.id.earAche);
		questionIds.add(R.id.soreThroat);
		questionIds.add(R.id.runnyNose);
		questionIds.add(R.id.stommacAche);
		questionIds.add(R.id.dryCough);
		questionIds.add(R.id.wetCough);
		questionIds.add(R.id.morningCough);
	}

	public WeekAnswers(Week myWeek) {
		super();
		this.week = myWeek;

		answers = new SparseBooleanArray(questionIds.size());
		setAnswer(R.id.generallyWell, true);
	}

	public boolean getAnswer(int id) {
		return answers.get(id);
	}

	public void setAnswer(int id, boolean value) {
		answers.put(id, value);
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("WeekAnswers{week:").append(this.week.toString());

		for (int id : questionIds) {
			buf.append(", ").append(NameFromIdHelper.getNameFromId(id)).append(":").append(this.getAnswer(id));
		}
		buf.append("}");

		return buf.toString();
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

		for (int id : questionIds) {
			if (this.getAnswer(id) != other.getAnswer(id)) return false;
		}

		if (this.week == null) {
			if (other.week != null)
				return false;
		} else if (!this.week.equals(other.week))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		for (int id : questionIds) {
			result = prime * result + (this.getAnswer(id) ? 1231 : 1237);
		}

		result = prime * result
				+ ((this.week == null) ? 0 : this.week.hashCode());

		return result;
	}

	public boolean getMalaise() {
		return getAnswer(R.id.malaise);
	}

	public boolean getFever() {
		return getAnswer(R.id.fever);
	}

	public boolean getEarAche() {
		return getAnswer(R.id.earAche);
	}

	public boolean getSoreThroat() {
		return getAnswer(R.id.soreThroat);
	}

	public boolean getRunnyNose() {
		return getAnswer(R.id.runnyNose);
	}

	public boolean getDryCough() {
		return getAnswer(R.id.dryCough);
	}

	public boolean getWetCough() {
		return getAnswer(R.id.wetCough);
	}

	public boolean getMorningCough() {
		return getAnswer(R.id.morningCough);
	}

	public boolean getStommacAche() {
		return getAnswer(R.id.stommacAche);
	}

	public boolean getGenerallyWell() {
		return getAnswer(R.id.generallyWell);
	}



}
