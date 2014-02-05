package se.fermitet.android.infektionsdagbok.model;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

import android.content.Context;

public class ModelManager {

	private Map<Week, WeekAnswers> weekAnswers;
	private Context context;

	public ModelManager(Context context) {
		super();
		this.context = context;
		this.weekAnswers = new HashMap<Week, WeekAnswers>();
	}

	public WeekAnswers getInitialWeekAnswers() {
		Week weekToUse = new Week(new DateTime());
		return getWeekAnswersForWeek(weekToUse);
	}

	public WeekAnswers getPreviousWeekAnswers(WeekAnswers initial) {
		return getWeekAnswersForWeek(initial.week.previous());
	}

	public WeekAnswers getNextWeekAnswers(WeekAnswers initial) {
		return getWeekAnswersForWeek(initial.week.next());
	}

	public void reset() {
		weekAnswers.clear();
	}

	private WeekAnswers getWeekAnswersForWeek(Week weekToUse) {
		WeekAnswers ret = weekAnswers.get(weekToUse);

		if (ret == null) {
			ret = new WeekAnswers(weekToUse);
			weekAnswers.put(weekToUse, ret);
		}

		return ret;
	}




}
