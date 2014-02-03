package se.fermitet.android.infektionsdagbok.storage;

import se.fermitet.android.infektionsdagbok.model.Week;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;

public class Storage {

	private static Storage instance;

	public static Storage instance() {
		if (instance == null) {
			instance = new Storage();
		}
		return instance;
	}

	private Storage() {
		super();
	}

	public WeekAnswers getAnswersForWeek(Week week) {
		return null;
	}

	public void saveAnswers(WeekAnswers toSave) {
		// TODO Auto-generated method stub

	}

	private String getFilename(Week week) {
		return week.toString() + ".dat";
	}

}
