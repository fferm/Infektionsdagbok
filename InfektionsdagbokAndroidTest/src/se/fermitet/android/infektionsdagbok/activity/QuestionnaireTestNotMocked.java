package se.fermitet.android.infektionsdagbok.activity;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;
import android.widget.TextView;

public class QuestionnaireTestNotMocked extends QuestionnaireTest {

	public QuestionnaireTestNotMocked() {
		super();
	}

	public void testInitials() throws Exception {
		assertInitialsOnQuestions();

		TextView weekView = (TextView) solo.getView(R.id.weekDisplay);
		DateTime now = new DateTime();
		assertEquals("week text", "" + now.getYear() + "-" + now.getWeekOfWeekyear(), weekView.getText());

		TextView mondayView = (TextView) solo.getView(R.id.mondayTV);
		assertEquals("monday text", getActivity().model.week.getMondayString(), mondayView.getText());

		TextView sundayView = (TextView) solo.getView(R.id.sundayTV);
		assertEquals("sunday text", getActivity().model.week.getSundayString(), sundayView.getText());

	}

	private void assertInitialsOnQuestions() {
		assertQuestionFullState(R.id.malaise, "Sjukdomskänsla", false);
		assertQuestionFullState(R.id.fever, "Feber > 38", false);
		assertQuestionFullState(R.id.earAche, "Öronvärk", false);
		assertQuestionFullState(R.id.soreThroat, "Halsont", false);
		assertQuestionFullState(R.id.runnyNose, "Snuva", false);
		assertQuestionFullState(R.id.stommacAche, "Magbesvär", false);
		assertQuestionFullState(R.id.dryCough, "Torrhosta", false);
		assertQuestionFullState(R.id.wetCough, "Slemhosta", false);
		assertQuestionFullState(R.id.morningCough, "Morgonupphostning", false);
		assertQuestionFullState(R.id.generallyWell, "Väsentligen frisk", true);
	}


	public void testClickingArrowNavigation() throws Exception {
		clickOnQuestionWithId(R.id.fever); // Change someting so that later check does not compare with default

		setStart();
		WeekAnswers beforeModel = null;
		boolean condition;
		do {
			beforeModel = getActivity().model;
			condition = beforeModel.getAnswer(R.id.fever);

			setElapsed();
		} while ( !condition && notYetTimeout());
		assertTrue("beforeModel fever should have changed", condition);


		solo.clickOnView(solo.getView(R.id.previousWeek));

		boolean sameCondition, equalsCondition, weekBeforeCondition;
		WeekAnswers afterModel;

		setStart();
		do {
			afterModel = getActivity().model;

			sameCondition = beforeModel == afterModel;
			equalsCondition = beforeModel.equals(afterModel);
			weekBeforeCondition = beforeModel.week.previous().equals(afterModel.week);

			setElapsed();
		} while ((sameCondition || equalsCondition || !weekBeforeCondition) && notYetTimeout());

		assertFalse("not same model", sameCondition);
		assertFalse("not equal models", equalsCondition);
		assertTrue("week before model", weekBeforeCondition);


		solo.clickOnView(solo.getView(R.id.nextWeek));

		WeekAnswers nextModel;

		setStart();
		do {
			nextModel = getActivity().model;

			equalsCondition = beforeModel.equals(nextModel);

			setElapsed();
		} while ( (!equalsCondition) && notYetTimeout());

		assertEquals("week answers equality when going back and forward", beforeModel, nextModel);
	}

}
