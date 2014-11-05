package se.fermitet.android.infektionsdagbok.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.joda.time.LocalDate;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.ModelManager;
import se.fermitet.android.infektionsdagbok.model.SickDay;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import se.fermitet.android.infektionsdagbok.model.Week;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;

public class DemoDataProvider  {
	private static int year = 2014;
	private static int maxWeek = 45;

	private ModelManager modelManager;

	private Collection<Integer> weeksWithGenerallyWell;
	private Collection<Integer> weeksWithMalaise;
	private Collection<Integer> weeksWithFever;
	private Collection<Integer> weeksWithEarAche;
	private Collection<Integer> weeksWithSoreThroat;
	private Collection<Integer> weeksWithRunnyNose;
	private Collection<Integer> weeksWithStommacAche;
	private Collection<Integer> weeksWithDryCough;
	private Collection<Integer> weeksWithWetCough;
	private Collection<Integer> weeksWithMorningCough;

	public DemoDataProvider(ModelManager modelManager) {
		super();

		this.modelManager = modelManager;

	}

	public void saveDemoData() throws Exception {
		setupWeekAnswers();

		modelManager.reset();
		modelManager.saveWeekAnswers(getDemoWeekAnswers());
		modelManager.saveAll(getDemoTreatments());
		modelManager.saveAll(getDemoSickDays());

	}


	private void setupWeekAnswers() {
		weeksWithGenerallyWell 	= Arrays.asList(6, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 26, 27, 28, 29, 30, 31, 32, 34, 35, 36, 37, 43, 44, 45);
		weeksWithMalaise		= Arrays.asList(1, 2, 3, 7, 8, 9, 10, 11, 24, 25, 39, 40, 41);
		weeksWithFever			= Arrays.asList(1, 3, 8, 10, 11, 12, 38 ,39);
		weeksWithEarAche		= Arrays.asList(38, 39, 31, 42);
		weeksWithSoreThroat		= Arrays.asList(7, 9, 28, 39, 46);
		weeksWithRunnyNose		= Arrays.asList(1, 3, 7, 8, 9, 11, 33, 38, 39, 43, 44, 46);
		weeksWithStommacAche	= Arrays.asList(12, 46);
		weeksWithDryCough		= Arrays.asList(3, 4, 5, 6, 9, 10, 24, 25, 39, 40, 41, 42);
		weeksWithWetCough		= Arrays.asList(1, 2, 3, 8, 9, 10, 11, 12, 24, 25, 38, 39, 40, 46);
		weeksWithMorningCough	= Arrays.asList(1, 43);
	}


	private Collection<WeekAnswers> getDemoWeekAnswers() {
		Collection<WeekAnswers> ret = new ArrayList<WeekAnswers>();
		for (int week = 1 ; week <= maxWeek; week++) {
			WeekAnswers wa = new WeekAnswers(new Week(year, week));

			if (!weeksWithGenerallyWell.contains(week)) wa.setAnswer(R.id.generallyWell, false);
			if (weeksWithMalaise.contains(week)) wa.setAnswer(R.id.malaise,  true);
			if (weeksWithFever.contains(week)) wa.setAnswer(R.id.fever,  true);
			if (weeksWithEarAche.contains(week)) wa.setAnswer(R.id.earAche,  true);
			if (weeksWithSoreThroat.contains(week)) wa.setAnswer(R.id.soreThroat,  true);
			if (weeksWithRunnyNose.contains(week)) wa.setAnswer(R.id.runnyNose,  true);
			if (weeksWithStommacAche.contains(week)) wa.setAnswer(R.id.stommacAche,  true);
			if (weeksWithDryCough.contains(week)) wa.setAnswer(R.id.dryCough,  true);
			if (weeksWithWetCough.contains(week)) wa.setAnswer(R.id.wetCough,  true);
			if (weeksWithMorningCough.contains(week)) wa.setAnswer(R.id.morningCough,  true);

			ret.add(wa);
		}

		return ret;
	}

	private Collection<Treatment> getDemoTreatments() {
		Collection<Treatment>  ret = new ArrayList<Treatment>();

		ret.add(new Treatment(new LocalDate(year - 1, 12, 30), 10, null, "Erymax"));
		ret.add(new Treatment(new LocalDate(year, 1, 10), 15, "Hemophilus", "Doxyferm x2"));
		ret.add(new Treatment(new LocalDate(year, 1, 24), 14, null, "Ciprofloxacin"));
		ret.add(new Treatment(new LocalDate(year, 6, 10), 5, null, "Oftaqulix"));
		ret.add(new Treatment(new LocalDate(year, 7, 17), 7, null, "Avelox"));
		ret.add(new Treatment(new LocalDate(year, 9, 26), 20, "Moraxella", "Avelox"));
		ret.add(new Treatment(new LocalDate(year, 10, 28), 10, "Moraxella", "Avelox"));

		return ret;
	}

	private Collection<SickDay> getDemoSickDays() {
		Collection<SickDay>  ret = new ArrayList<SickDay>();

		ret.add(new SickDay(new LocalDate(year, 1, 20), new LocalDate(year, 1, 24)));
		ret.add(new SickDay(new LocalDate(year, 2, 20), new LocalDate(year, 2, 27)));
		ret.add(new SickDay(new LocalDate(year, 3, 4), new LocalDate(year, 4, 10)));
		ret.add(new SickDay(new LocalDate(year, 9, 26), new LocalDate(year, 9, 30)));
		ret.add(new SickDay(new LocalDate(year, 11, 14), null));

		return ret;
	}

}
