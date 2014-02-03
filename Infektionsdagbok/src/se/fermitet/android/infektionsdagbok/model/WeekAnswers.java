package se.fermitet.android.infektionsdagbok.model;

import org.json.JSONException;
import org.json.JSONObject;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.helper.NameFromIdHelper;

public class WeekAnswers {

	private static final String JSON_WEEK_STRING = "week";

	public Week week;

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

	public WeekAnswers(Week myWeek) {
		super();
		this.week = myWeek;
	}

	public WeekAnswers(String jsonString) throws JSONException {
		super();

		JSONObject json = new JSONObject(jsonString);

		String weekString = json.getString(JSON_WEEK_STRING);
		this.week = new Week(weekString);

		this.generallyWell = json.getBoolean(NameFromIdHelper.getNameFromId(R.id.generallyWell));
		this.malaise = json.getBoolean(NameFromIdHelper.getNameFromId(R.id.malaise));
		this.fever = json.getBoolean(NameFromIdHelper.getNameFromId(R.id.fever));
		this.earAche = json.getBoolean(NameFromIdHelper.getNameFromId(R.id.earAche));
		this.soreThroat = json.getBoolean(NameFromIdHelper.getNameFromId(R.id.soreThroat));
		this.runnyNose = json.getBoolean(NameFromIdHelper.getNameFromId(R.id.runnyNose));
		this.stommacAche = json.getBoolean(NameFromIdHelper.getNameFromId(R.id.stommacAche));
		this.dryCough = json.getBoolean(NameFromIdHelper.getNameFromId(R.id.dryCough));
		this.wetCough = json.getBoolean(NameFromIdHelper.getNameFromId(R.id.wetCough));
		this.morningCough = json.getBoolean(NameFromIdHelper.getNameFromId(R.id.morningCough));
	}

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
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("WeekAnswers{week:").append(this.week.toString());
		buf.append(", generallyWell:").append(this.generallyWell);
		buf.append(", malaise:").append(this.malaise);
		buf.append(", fever:").append(this.fever);
		buf.append(", earAche:").append(this.earAche);
		buf.append(", soreThroat:").append(this.soreThroat);
		buf.append(", runnyNose:").append(this.runnyNose);
		buf.append(", stommacAche:").append(this.stommacAche);
		buf.append(", dryCough:").append(this.dryCough);
		buf.append(", wetCough:").append(this.wetCough);
		buf.append(", morningCough:").append(this.morningCough);
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

	public String toJSON() throws JSONException {
		JSONObject json = new JSONObject();

		json.put(JSON_WEEK_STRING, week.toString());

		putJSONAnswer(json, R.id.generallyWell);
		putJSONAnswer(json, R.id.malaise);
		putJSONAnswer(json, R.id.fever);
		putJSONAnswer(json, R.id.earAche);
		putJSONAnswer(json, R.id.soreThroat);
		putJSONAnswer(json, R.id.runnyNose);
		putJSONAnswer(json, R.id.stommacAche);
		putJSONAnswer(json, R.id.dryCough);
		putJSONAnswer(json, R.id.wetCough);
		putJSONAnswer(json, R.id.morningCough);

		return json.toString();
	}

	private void putJSONAnswer(JSONObject json, int id) throws JSONException {
		json.put(NameFromIdHelper.getNameFromId(id), this.getAnswer(id));
	}



}
