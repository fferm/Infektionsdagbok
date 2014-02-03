package se.fermitet.android.infektionsdagbok.model;

import junit.framework.TestCase;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.helper.NameFromIdHelper;

public class WeekAnswersTest extends TestCase {

	private WeekAnswers wa;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		wa = new WeekAnswers(new Week(new DateTime()));
	}

	public void testDefaultConstructor() throws Exception {
		assertNotNull("not null", wa);
	}

	public void testWeekConstructor() throws Exception {
		Week myWeek = new Week(new DateTime().plusWeeks(2));
		WeekAnswers wa = new WeekAnswers(myWeek);

		assertEquals(myWeek, wa.week);
	}

	public void testDefaultValues() throws Exception {
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

	public void testGettersAndSetters() throws Exception {
		assertGetterAndSetterForOneAnswer(R.id.generallyWell);
		assertGetterAndSetterForOneAnswer(R.id.malaise);
		assertGetterAndSetterForOneAnswer(R.id.fever);
		assertGetterAndSetterForOneAnswer(R.id.earAche);
		assertGetterAndSetterForOneAnswer(R.id.soreThroat);
		assertGetterAndSetterForOneAnswer(R.id.runnyNose);
		assertGetterAndSetterForOneAnswer(R.id.stommacAche);
		assertGetterAndSetterForOneAnswer(R.id.dryCough);
		assertGetterAndSetterForOneAnswer(R.id.wetCough);
		assertGetterAndSetterForOneAnswer(R.id.morningCough);
	}

	private void assertGetterAndSetterForOneAnswer(int id) {
		boolean before = wa.getAnswer(id);
		wa.setAnswer(id, !before);
		assertTrue(NameFromIdHelper.getNameFromId(id) + " setter", wa.getAnswer(id) != before);
	}

	public void testValueObject() throws Exception {
		Week otherWeek = new Week(new DateTime().plusMonths(1));

		WeekAnswers orig = new WeekAnswers(new Week(new DateTime()));
		WeekAnswers nonModified = new WeekAnswers(new Week(new DateTime()));

		WeekAnswers modWeek = new WeekAnswers(new Week(new DateTime()));
		modWeek.week = otherWeek;

		WeekAnswers modAnswer = new WeekAnswers(new Week(new DateTime()));
		modAnswer.setAnswer(R.id.generallyWell, false);
		modAnswer.setAnswer(R.id.malaise, true);

		assertTrue("Equal to itself", orig.equals(orig));
		assertTrue("Equal to non modified", orig.equals(nonModified));
		assertFalse("Not equal to one with modified week", orig.equals(modWeek));
		assertFalse("Not equal to one with modified answer", orig.equals(modAnswer));
		assertFalse("Not equal to null", orig.equals(null));
		assertFalse("Not equal to object of other class", orig.equals("STRING"));

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

	public void testJSONWriteAndRead() throws Exception {
		// Change some answers to get away from default
		wa.setAnswer(R.id.generallyWell, false);
		wa.setAnswer(R.id.malaise, true);
		wa.setAnswer(R.id.morningCough, true);

		String json = wa.toJSON();

		WeekAnswers fromJSON = WeekAnswers.fromJSON(json);

		assertEquals(wa, fromJSON);
	}
}
