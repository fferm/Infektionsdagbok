package se.fermitet.android.infektionsdagbok.model;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.storage.Storage;
import android.content.Context;

public class ModelManager {

	private Storage storage;

	public ModelManager(Context context) {
		super();
		this.storage = new Storage(context);
	}

	public void setStorage(Storage storage) {
		this.storage = storage;
	}


	public WeekAnswers getInitialWeekAnswers() {
		Week weekToUse = new Week(new DateTime());
		return getWeekAnswersForWeek(weekToUse);
	}

	public WeekAnswers getPreviousWeekAnswers(WeekAnswers initial) throws Exception {
		storage.saveAnswers(initial);

		return getWeekAnswersForWeek(initial.week.previous());
	}

	public WeekAnswers getNextWeekAnswers(WeekAnswers initial) throws Exception {
		storage.saveAnswers(initial);

		return getWeekAnswersForWeek(initial.week.next());
	}

	public void reset() {
		storage.clear();
	}

	private WeekAnswers getWeekAnswersForWeek(Week weekToUse) {
		WeekAnswers ret = null;
		try {
			ret = storage.getAnswersForWeek(weekToUse);

			if (ret == null) {
				ret = new WeekAnswers(weekToUse);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}
}
