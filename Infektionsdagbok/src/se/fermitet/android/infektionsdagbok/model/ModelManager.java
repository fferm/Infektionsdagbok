package se.fermitet.android.infektionsdagbok.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public WeekAnswers getInitialWeekAnswers() throws Exception {
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

	public Map<Week, WeekAnswers> getAllWeekAnswersInYear(int year) throws Exception {
		Map<Week, WeekAnswers> ret = new HashMap<Week, WeekAnswers>();

		Week week = new Week("2014-01");
		while (week.year() == year) {
			WeekAnswers retrieved = storage.getAnswersForWeek(week);

			if (retrieved != null) {
				ret.put(week, retrieved);
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

	public WeekAnswers getEarliestWeekAnswers() throws Exception {
		WeekAnswers earliestAnswers = null;

		for (WeekAnswers weekAnswers : storage.getAllAnswers()) {
			if (earliestAnswers == null || weekAnswers.week.isBefore(earliestAnswers.week)) {
				earliestAnswers = weekAnswers;
			}
		}

		return earliestAnswers;
	}

	private WeekAnswers getWeekAnswersForWeekCreateIfNeeded(Week weekToUse) throws Exception {
		WeekAnswers ret = null;
		ret = storage.getAnswersForWeek(weekToUse);

		if (ret == null) {
			ret = new WeekAnswers(weekToUse);
		}


		return ret;
	}

	public Collection<Treatment> getAllTreatments() throws Exception {
		return storage.getAllTreatments();
	}

	public void saveTreatments(Collection<Treatment> toSave) throws Exception {
		storage.saveTreatments(toSave);
	}

}
