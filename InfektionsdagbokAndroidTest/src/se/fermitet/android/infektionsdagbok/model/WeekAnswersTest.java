package se.fermitet.android.infektionsdagbok.model;

import junit.framework.TestCase;

import org.joda.time.LocalDate;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.helper.NameFromIdHelper;

public class WeekAnswersTest extends TestCase {

	private WeekAnswers wa;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		wa = new WeekAnswers(new Week(new LocalDate()));
	}

	public void testDefaultConstructor() throws Exception {
		assertNotNull("not null", wa);
	}

	public void testWeekConstructor() throws Exception {
		Week myWeek = new Week(new LocalDate().plusWeeks(2));
		WeekAnswers wa = new WeekAnswers(myWeek);

		assertEquals(myWeek, wa.week);
	}

	public void testDefaultValues() throws Exception {
		for (int id : WeekAnswers.questionIds) {
			if (id != R.id.generallyWell) {
				assertFalse(NameFromIdHelper.getNameFromId(id), wa.getAnswer(id));
			} else {
				assertTrue(NameFromIdHelper.getNameFromId(id), wa.getAnswer(id));
			}
		}

		assertEquals("week", new Week(new LocalDate()), wa.week);
	}

	public void testGettersAndSetters() throws Exception {
		for (int id : WeekAnswers.questionIds) {
			assertGetterAndSetterForOneAnswer(id);
		}
	}

	private void assertGetterAndSetterForOneAnswer(int id) {
		boolean before = wa.getAnswer(id);
		wa.setAnswer(id, !before);
		assertTrue(NameFromIdHelper.getNameFromId(id) + " setter", wa.getAnswer(id) != before);
	}

	public void testValueObject() throws Exception {
		Week otherWeek = new Week(new LocalDate().plusMonths(1));

		WeekAnswers orig = new WeekAnswers(new Week(new LocalDate()));
		WeekAnswers nonModified = new WeekAnswers(new Week(new LocalDate()));

		WeekAnswers modWeek = new WeekAnswers(new Week(new LocalDate()));
		modWeek.week = otherWeek;

		WeekAnswers modAnswer = new WeekAnswers(new Week(new LocalDate()));
		modAnswer.setAnswer(R.id.fever, !modAnswer.getFever());
		modAnswer.setAnswer(R.id.malaise, !modAnswer.getMalaise());

		WeekAnswers modGenerallyWell = new WeekAnswers(new Week(LocalDate.now()));
		modGenerallyWell.setAnswer(R.id.generallyWell, !modGenerallyWell.getGenerallyWell());

		assertTrue("Equal to itself", orig.equals(orig));
		assertTrue("Equal to non modified", orig.equals(nonModified));
		assertFalse("Not equal to one with modified week", orig.equals(modWeek));
		assertFalse("Not equal to one with modified answer", orig.equals(modAnswer));
		assertFalse("Not equal to null", orig.equals(null));
		assertFalse("Not equal to object of other class", orig.equals("STRING"));
		assertFalse("Not equal to object with changed value in Generally well", orig.equals(modGenerallyWell));
		assertTrue("non modified hash code", orig.hashCode() == nonModified.hashCode());
	}

	public void testToString() throws Exception {
		StringBuffer buf = new StringBuffer();
		buf.append("WeekAnswers{week:").append(wa.week.toString());
		buf.append(", generallyWell:").append(wa.getAnswer(R.id.generallyWell));
		buf.append(", malaise:").append(wa.getAnswer(R.id.malaise));
		buf.append(", fever:").append(wa.getAnswer(R.id.fever));
		buf.append(", earAche:").append(wa.getAnswer(R.id.earAche));
		buf.append(", soreThroat:").append(wa.getAnswer(R.id.soreThroat));
		buf.append(", runnyNose:").append(wa.getAnswer(R.id.runnyNose));
		buf.append(", stommacAche:").append(wa.getAnswer(R.id.stommacAche));
		buf.append(", dryCough:").append(wa.getAnswer(R.id.dryCough));
		buf.append(", wetCough:").append(wa.getAnswer(R.id.wetCough));
		buf.append(", morningCough:").append(wa.getAnswer(R.id.morningCough));
		buf.append("}");

		assertEquals(buf.toString(), wa.toString());
	}

	public void testQuestionAccessors() throws Exception {
		wa = new WeekAnswers(new Week(new LocalDate()));
		assertFalse("Malaise before", wa.getMalaise());
		wa.setAnswer(R.id.malaise, true);
		assertTrue("Malaise after", wa.getMalaise());

		wa = new WeekAnswers(new Week(new LocalDate()));
		assertFalse("Fever before", wa.getFever());
		wa.setAnswer(R.id.fever, true);
		assertTrue("Fever after", wa.getFever());

		wa = new WeekAnswers(new Week(new LocalDate()));
		assertFalse("EarAche before", wa.getEarAche());
		wa.setAnswer(R.id.earAche, true);
		assertTrue("EarAche after", wa.getEarAche());

		wa = new WeekAnswers(new Week(new LocalDate()));
		assertFalse("SoreThroat before", wa.getSoreThroat());
		wa.setAnswer(R.id.soreThroat, true);
		assertTrue("SoreThroat after", wa.getSoreThroat());

		wa = new WeekAnswers(new Week(new LocalDate()));
		assertFalse("RunnyNose before", wa.getRunnyNose());
		wa.setAnswer(R.id.runnyNose, true);
		assertTrue("RunnyNose after", wa.getRunnyNose());

		wa = new WeekAnswers(new Week(new LocalDate()));
		assertFalse("StommacAche before", wa.getStommacAche());
		wa.setAnswer(R.id.stommacAche, true);
		assertTrue("StommacAche after", wa.getStommacAche());

		wa = new WeekAnswers(new Week(new LocalDate()));
		assertFalse("DryCough before", wa.getDryCough());
		wa.setAnswer(R.id.dryCough, true);
		assertTrue("DryCough after", wa.getDryCough());

		wa = new WeekAnswers(new Week(new LocalDate()));
		assertFalse("WetCough before", wa.getWetCough());
		wa.setAnswer(R.id.wetCough, true);
		assertTrue("WetCough after", wa.getWetCough());

		wa = new WeekAnswers(new Week(new LocalDate()));
		assertFalse("MorningCough before", wa.getMorningCough());
		wa.setAnswer(R.id.morningCough, true);
		assertTrue("MorningCough after", wa.getMorningCough());

		wa = new WeekAnswers(new Week(new LocalDate()));
		assertTrue("GenerallyWell before", wa.getGenerallyWell());
		wa.setAnswer(R.id.generallyWell, false);
		assertFalse("GenerallyWell after", wa.getGenerallyWell());

	}
}
