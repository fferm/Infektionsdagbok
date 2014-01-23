package se.fermitet.android.infektionsdagbok.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.junit.Test;

public class WeekAnswersTest {

	@Test
	public void classExists() throws Exception {
		WeekAnswers wa = new WeekAnswers();
		assertNotNull("not null", wa);
	}

	@Test
	public void defaultValues() throws Exception {
		WeekAnswers wa = new WeekAnswers();
		assertTrue("generally well", wa.generallyWell);
		assertFalse("malaise", wa.malaise);
		assertFalse("fever", wa.fever);
		assertFalse("earAche", wa.earAche);
		assertFalse("soreThroat", wa.soreThroat);
		assertFalse("runnyNose", wa.runnyNose);
		assertFalse("stommacAche", wa.stommacAche);
		assertFalse("dryCough", wa.dryCough);
		assertFalse("wetCough", wa.wetCough);
		assertFalse("morningCough", wa.morningCough);

		assertEquals("week", new Week(new DateTime()), wa.week);
	}

	@Test
	public void valueObject() throws Exception {
		Week thisWeek = new Week(new DateTime());
		Week otherWeek = new Week(new DateTime().plusMonths(1));

		WeekAnswers orig = new WeekAnswers();
		WeekAnswers nonModified = new WeekAnswers();

		WeekAnswers modWeek = new WeekAnswers();
		modWeek.week = otherWeek;

		WeekAnswers modAnswer = new WeekAnswers();
		modAnswer.malaise = true;

		assertTrue("Equal to itself", orig.equals(orig));
		assertTrue("Equal to non modified", orig.equals(nonModified));
		assertFalse("Not equal to one with modified week", orig.equals(modWeek));
		assertFalse("Not equal to one with modified answer", orig.equals(modAnswer));
		assertFalse("Not equal to null", orig.equals(null));
		assertFalse("Not equal to object of other class", orig.equals("STRING"));

		assertTrue("non modified hash code", orig.hashCode() == nonModified.hashCode());


	}
}
