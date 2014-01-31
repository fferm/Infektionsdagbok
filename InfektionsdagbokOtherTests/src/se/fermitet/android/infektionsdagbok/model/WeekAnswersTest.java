package se.fermitet.android.infektionsdagbok.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.junit.Test;

import se.fermitet.android.infektionsdagbok.NameFromIdHelper;
import se.fermitet.android.infektionsdagbok.R;

public class WeekAnswersTest {

	@Test
	public void defaultConstructor() throws Exception {
		WeekAnswers wa = new WeekAnswers();
		assertNotNull("not null", wa);
	}

	@Test
	public void weekConstructor() throws Exception {
		Week myWeek = new Week(new DateTime().plusWeeks(2));
		WeekAnswers wa = new WeekAnswers(myWeek);

		assertEquals(myWeek, wa.week);
	}

	@Test
	public void defaultValues() throws Exception {
		WeekAnswers wa = new WeekAnswers();
		assertTrue("generally well", wa.getAnswer(R.id.generallyWell));
		assertFalse("malaise", wa.getAnswer(R.id.malaise));
		assertFalse("fever", wa.getAnswer(R.id.fever));
		assertFalse("earAche", wa.getAnswer(R.id.earAche));
		assertFalse("soreThroat", wa.getAnswer(R.id.soreThroat));
		assertFalse("runnyNose", wa.getAnswer(R.id.runnyNose));
		assertFalse("stommacAche", wa.getAnswer(R.id.stommacAche));
		assertFalse("dryCough", wa.getAnswer(R.id.dryCough));
		assertFalse("wetCough", wa.getAnswer(R.id.wetCough));
		assertFalse("morningCough", wa.getAnswer(R.id.morningCough));

		assertEquals("week", new Week(new DateTime()), wa.week);
	}

	@Test
	public void gettersAndSetters() throws Exception {
		testGetterAndSetterForOneAnswer(R.id.generallyWell);
		testGetterAndSetterForOneAnswer(R.id.malaise);
		testGetterAndSetterForOneAnswer(R.id.fever);
		testGetterAndSetterForOneAnswer(R.id.earAche);
		testGetterAndSetterForOneAnswer(R.id.soreThroat);
		testGetterAndSetterForOneAnswer(R.id.runnyNose);
		testGetterAndSetterForOneAnswer(R.id.stommacAche);
		testGetterAndSetterForOneAnswer(R.id.dryCough);
		testGetterAndSetterForOneAnswer(R.id.wetCough);
		testGetterAndSetterForOneAnswer(R.id.morningCough);
	}

	private void testGetterAndSetterForOneAnswer(int id) {
		WeekAnswers wa = new WeekAnswers();

		boolean before = wa.getAnswer(id);
		wa.setAnswer(id, !before);
		assertTrue(NameFromIdHelper.getNameFromId(id) + " setter", wa.getAnswer(id) != before);
	}

	@Test
	public void valueObject() throws Exception {
		Week otherWeek = new Week(new DateTime().plusMonths(1));

		WeekAnswers orig = new WeekAnswers();
		WeekAnswers nonModified = new WeekAnswers();

		WeekAnswers modWeek = new WeekAnswers();
		modWeek.week = otherWeek;

		WeekAnswers modAnswer = new WeekAnswers();
		modAnswer.setAnswer(R.id.malaise, true);

		assertTrue("Equal to itself", orig.equals(orig));
		assertTrue("Equal to non modified", orig.equals(nonModified));
		assertFalse("Not equal to one with modified week", orig.equals(modWeek));
		assertFalse("Not equal to one with modified answer", orig.equals(modAnswer));
		assertFalse("Not equal to null", orig.equals(null));
		assertFalse("Not equal to object of other class", orig.equals("STRING"));

		assertTrue("non modified hash code", orig.hashCode() == nonModified.hashCode());
	}
}
