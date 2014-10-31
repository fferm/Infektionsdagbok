package se.fermitet.android.infektionsdagbok.storage;

import java.util.UUID;

import org.joda.time.LocalDate;
import org.json.JSONException;
import org.json.JSONObject;

import se.fermitet.android.infektionsdagbok.helper.NameFromIdHelper;
import se.fermitet.android.infektionsdagbok.model.ModelObjectBase;
import se.fermitet.android.infektionsdagbok.model.SickDay;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import se.fermitet.android.infektionsdagbok.model.Week;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;

public class Jsonizer {

	private static final String WEEK_ANSWERS_WEEK = "week";

	private static final String MODEL_OBJECT_BASE_UUID = "uuid";

	private static final String TREATMENT_INFECTION_TYPE = "infectionType";
	private static final String TREATMENT_MEDICINE = "medicine";
	private static final String TREATMENT_STARTING_DATE = "startingDate";
	private static final String TREATMENT_NUM_DAYS = "numDays";

	private static final String SICK_DAY_START = "start";
	private static final String SICK_DAY_END = "end";

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

	public static String treatmentToJSON(Treatment obj) throws JSONException {
		JSONObject json = new JSONObject();

		addModelObjectBaseItemsToJSON(obj, json);

		json.put(TREATMENT_INFECTION_TYPE, obj.getInfectionType());
		json.put(TREATMENT_MEDICINE, obj.getMedicine());
		json.put(TREATMENT_STARTING_DATE, getJsonObjectForDate(obj.getStartingDate()));
		json.put(TREATMENT_NUM_DAYS, obj.getNumDays());

		return json.toString();
	}

	public static Treatment treatmentFromJSON(String jsonTxt) throws JSONException {
		JSONObject json = new JSONObject(jsonTxt);
		Treatment obj = new Treatment();

		setModelObjectBaseValuesFromJSON(obj, json);

		if (! json.isNull(TREATMENT_INFECTION_TYPE)) obj.setInfectionType(json.getString(TREATMENT_INFECTION_TYPE));
		if (! json.isNull(TREATMENT_MEDICINE)) obj.setMedicine(json.getString(TREATMENT_MEDICINE));
		if (! json.isNull(TREATMENT_STARTING_DATE)) obj.setStartingDate(getDateFromJsonObject(json.getJSONObject(TREATMENT_STARTING_DATE)));
		if (! json.isNull(TREATMENT_NUM_DAYS)) obj.setNumDays(json.getInt(TREATMENT_NUM_DAYS));

		return obj;
	}

	public static String sickDayToJSON(SickDay obj) throws JSONException {
		JSONObject json = new JSONObject();

		addModelObjectBaseItemsToJSON(obj, json);

		json.put(SICK_DAY_START, getJsonObjectForDate(obj.getStart()));
		json.put(SICK_DAY_END, getJsonObjectForDate(obj.getEnd()));

		return json.toString();
	}

	public static SickDay sickDayFromJSON(String jsonTxt) throws JSONException {
		JSONObject json = new JSONObject(jsonTxt);
		SickDay obj = new SickDay();

		setModelObjectBaseValuesFromJSON(obj, json);

		if (! json.isNull(SICK_DAY_START)) obj.setStart(getDateFromJsonObject(json.getJSONObject(SICK_DAY_START)));
		if (! json.isNull(SICK_DAY_END)) obj.setEnd(getDateFromJsonObject(json.getJSONObject(SICK_DAY_END)));

		return obj;
	}

	private static LocalDate getDateFromJsonObject(JSONObject json) throws JSONException {
		int year = json.getInt(DATE_YEAR);
		int month = json.getInt(DATE_MONTH);
		int day = json.getInt(DATE_DAY);

		return new LocalDate(year, month, day);
	}

	private static JSONObject getJsonObjectForDate(LocalDate date) throws JSONException {
		if (date == null) return null;

		JSONObject ret = new JSONObject();

		ret.put(DATE_YEAR, date.year().get());
		ret.put(DATE_MONTH, date.monthOfYear().get());
		ret.put(DATE_DAY, date.dayOfMonth().get());

		return ret;
	}


	private static void addModelObjectBaseItemsToJSON(ModelObjectBase obj, JSONObject json) throws JSONException {
		json.put(MODEL_OBJECT_BASE_UUID, obj.getUUID().toString());
	}

	private static void setModelObjectBaseValuesFromJSON(ModelObjectBase obj, JSONObject json) throws JSONException {
		if (! json.isNull(MODEL_OBJECT_BASE_UUID)) obj.setUUID(UUID.fromString(json.getString(MODEL_OBJECT_BASE_UUID)));
	}



}

