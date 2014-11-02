package se.fermitet.android.infektionsdagbok.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.joda.time.LocalDate;

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
		Week weekToUse = new Week(new LocalDate());
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

	@SuppressWarnings("unchecked")
	public Map<UUID, Treatment> getAllTreatments() throws Exception {
		return (Map<UUID, Treatment>) getAllItemsOfClass(Treatment.class);
	}

	@SuppressWarnings("unchecked")
	public Map<UUID, SickDay> getAllSickDays() throws Exception {
		return (Map<UUID, SickDay>) getAllItemsOfClass(SickDay.class);
	}

	public Map<UUID, ? extends ModelObjectBase> getAllItemsOfClass(Class<? extends ModelObjectBase> clz) throws Exception {
		if (clz.equals(Treatment.class))
			return storage.getAllTreatments();
		else if (clz.equals(SickDay.class))
			return storage.getAllSickDays();
		else throw new IllegalArgumentException("Class needs to be a subclass of ModelObjectBase.  Was: " + clz.getName());
	}

	public void saveAll(Collection<? extends ModelObjectBase> toSave) throws Exception {
		Map<Class<? extends ModelObjectBase>, Collection<ModelObjectBase>> classSplitMap = new HashMap<Class<? extends ModelObjectBase>, Collection<ModelObjectBase>>();

		for (ModelObjectBase obj : toSave) {
			Collection<ModelObjectBase> classCollection = null;
			Class<? extends ModelObjectBase> key = obj.getClass();

			if (classSplitMap.containsKey(key)) {
				classCollection = classSplitMap.get(key);
			} else {
				classCollection = new ArrayList<ModelObjectBase>();
				classSplitMap.put(key, classCollection);
			}
			classCollection.add(obj);
		}

		for (Class<? extends ModelObjectBase> clz : classSplitMap.keySet()) {
			saveAllForClass(classSplitMap.get(clz), clz);
		}
	}

	@SuppressWarnings("unchecked")
	private void saveAllForClass(Collection<? extends ModelObjectBase> collection, Class<? extends ModelObjectBase> clz) throws Exception {
		if (clz.equals(Treatment.class)) {
			storage.saveTreatments((Collection<Treatment>) collection);
		} else if (clz.equals(SickDay.class)) {
			storage.saveSickDays((Collection<SickDay>) collection);
		}

		else throw new IllegalArgumentException("Unknown class: " + clz);
	}


	public void save(ModelObjectBase obj) throws Exception {
		@SuppressWarnings("unchecked")
		Map<UUID, ModelObjectBase> alreadySaved = (Map<UUID, ModelObjectBase>) getAllItemsOfClass(obj.getClass());

		alreadySaved.put(obj.getUUID(), obj);

		saveAllForClass(alreadySaved.values(), obj.getClass());
	}


	public void delete(ModelObjectBase obj) throws Exception {
		@SuppressWarnings("unchecked")
		Map<UUID, ModelObjectBase> alreadySaved = (Map<UUID, ModelObjectBase>) getAllItemsOfClass(obj.getClass());

		alreadySaved.remove(obj.getUUID());

		saveAllForClass(alreadySaved.values(), obj.getClass());
	}


}
