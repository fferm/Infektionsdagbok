package se.fermitet.android.infektionsdagbok.model;

public class ModelManager {

	private static ModelManager singletonInstance;

	public static ModelManager instance() {
		if (singletonInstance == null) {
			singletonInstance = new ModelManager();
		}
		return singletonInstance;
	}

	private ModelManager() {
		super();
	}

	public WeekAnswers getInitialWeekAnswers() {
		return new WeekAnswers();
		// TODO: Store
	}

	public WeekAnswers getPreviousWeekAnswers(WeekAnswers initial) {
		return new WeekAnswers(initial.week.previous());
	}

	public WeekAnswers getNextWeekAnswers(WeekAnswers initial) {
		return new WeekAnswers(initial.week.next());
	}

}
