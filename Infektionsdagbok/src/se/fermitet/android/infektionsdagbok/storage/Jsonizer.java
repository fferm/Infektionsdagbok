package se.fermitet.android.infektionsdagbok.storage;

import java.util.UUID;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import se.fermitet.android.infektionsdagbok.helper.NameFromIdHelper;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import se.fermitet.android.infektionsdagbok.model.Week;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;

public class Jsonizer {

	private static final String WEEK_ANSWERS_WEEK = "week";
	private static final String TREATMENT_UUID = "uuid";
	private static final String TREATMENT_INFECTION_TYPE = "infectionType";
	private static final String TREATMENT_MEDICINE = "medicine";
	private static final String TREATMENT_STARTING_DATE = "startingDate";
	private static final String TREATMENT_NUM_DAYS = "numDays";
	private static final String DATE_YEAR = "year";
	private static final String DATE_MONTH = "month";
	private static final String DATE_DAY = "day";

	public static String weekAnswersToJSON(WeekAnswers wa) throws JSONException {
		JSONObject json = new JSONObject();

		json.put(WEEK_ANSWERS_WEEK, wa.week.toString());

		for (int id : WeekAnswers.questionIds) {
			json.put(NameFromIdHelper.getNameFromId(id), wa.getAnswer(id));
		}

		return json.toString();
	}

	public static WeekAnswers weekAnswersFromJSON(String weekAnswersJSON) throws JSONException {
		JSONObject json = new JSONObject(weekAnswersJSON);

		String weekString = json.getString(WEEK_ANSWERS_WEEK);

		WeekAnswers ret = new WeekAnswers(new Week(weekString));

		for (Integer id : WeekAnswers.questionIds) {
			ret.setAnswer(id, json.getBoolean(NameFromIdHelper.getNameFromId(id)));
		}

		return ret;
	}
	
	public static String treatmentToJSON(Treatment treatment) throws JSONException {
		JSONObject treatmentJson = new JSONObject();
		
		treatmentJson.put(TREATMENT_UUID, treatment.getUUID().toString());
		treatmentJson.put(TREATMENT_INFECTION_TYPE, treatment.getInfectionType());
		treatmentJson.put(TREATMENT_MEDICINE, treatment.getMedicine());
		if (treatment.getStartingDate() != null) {
			treatmentJson.put(TREATMENT_STARTING_DATE, getJsonObjectForDateWithoutTime(treatment.getStartingDate()));
		}
		treatmentJson.put(TREATMENT_NUM_DAYS, treatment.getNumDays());

		return treatmentJson.toString();
	}

	public static Treatment treatmentFromJSON(String treatmentJson) throws JSONException {
		JSONObject json = new JSONObject(treatmentJson);
		Treatment treatment = new Treatment();
		
		if (! json.isNull(TREATMENT_UUID)) treatment.setUUID(UUID.fromString(json.getString(TREATMENT_UUID)));
		if (! json.isNull(TREATMENT_INFECTION_TYPE)) treatment.setInfectionType(json.getString(TREATMENT_INFECTION_TYPE));
		if (! json.isNull(TREATMENT_MEDICINE)) treatment.setMedicine(json.getString(TREATMENT_MEDICINE));
		if (! json.isNull(TREATMENT_STARTING_DATE)) treatment.setStartingDate(getDateWithoutTimeFromJsonObject(json.getJSONObject(TREATMENT_STARTING_DATE)));
		if (! json.isNull(TREATMENT_NUM_DAYS)) treatment.setNumDays(json.getInt(TREATMENT_NUM_DAYS));
		
		return treatment;
	}
	
	private static DateTime getDateWithoutTimeFromJsonObject(JSONObject json) throws JSONException {
		int year = json.getInt(DATE_YEAR);
		int month = json.getInt(DATE_MONTH);
		int day = json.getInt(DATE_DAY);
		
		return new DateTime(year, month, day, 0, 0).withMillisOfDay(0);
	}

	private static JSONObject getJsonObjectForDateWithoutTime(DateTime date) throws JSONException {
		JSONObject ret = new JSONObject();
		
		ret.put(DATE_YEAR, date.year().get());
		ret.put(DATE_MONTH, date.monthOfYear().get());
		ret.put(DATE_DAY, date.dayOfMonth().get());
		
		return ret;
	}


}

