package se.fermitet.android.infektionsdagbok.model;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

public class ModelManager {

	private Map<Week, WeekAnswers> weekAnswers;

	private static ModelManager singletonInstance;
	public static ModelManager instance() {
		if (singletonInstance == null) {
			singletonInstance = new ModelManager();
		}
		return singletonInstance;
	}

	private ModelManager() {
		super();
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
