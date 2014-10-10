package se.fermitet.android.infektionsdagbok.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.storage.Storage;

/**
 * Used to manage all model objects.  The storage of these are not handled here, but in the Storage class instead.
 * @author feffe
 *
 */
public class ModelManager {

	private Storage storage;

	public ModelManager(Storage storage) {
		super();
		this.storage = storage;
	}

	public Storage getStorage() {
		return this.storage;
	}

	public WeekAnswers getInitialWeekAnswers() {
		Week weekToUse = new Week(new DateTime());
		return getWeekAnswersForWeekCreateIfNeeded(weekToUse);
	}

	public WeekAnswers getPreviousWeekAnswers(WeekAnswers initial) throws Exception {
		storage.saveAnswers(initial);

		return getWeekAnswersForWeekCreateIfNeeded(initial.week.previous());
	}

	public WeekAnswers getNextWeekAnswers(WeekAnswers initial) throws Exception {
		storage.saveAnswers(initial);

		return getWeekAnswersForWeekCreateIfNeeded(initial.week.next());
	}

	public void reset() {
		storage.clear();
	}

	public List<WeekAnswers> getAllWeekAnswersInYear(int year) throws Exception {
		List<WeekAnswers> ret = new ArrayList<WeekAnswers>();

		Week week = new Week("2014-01");
		while (week.year() == year) {
			WeekAnswers retrieved = storage.getAnswersForWeek(week);

			if (retrieved != null) {
				ret.add(retrieved);
			}
			week = week.next();
		}

		return ret;
	}

	public void saveWeekAnswers(Collection<WeekAnswers> list) throws Exception {
		List<Week> previousWeeks = new ArrayList<Week>();

		for (WeekAnswers wa : list) {
			Week week = wa.week;
			if (previousWeeks.contains(week)) {
				throw new Exception("Double WeekAnswers objects for the same week: " + week.toString());
			}
			previousWeeks.add(week);
		}

		for (WeekAnswers wa : list) {
			storage.saveAnswers(wa);
		}
	}

	private WeekAnswers getWeekAnswersForWeekCreateIfNeeded(Week weekToUse) {
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
