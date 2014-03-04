package se.fermitet.android.infektionsdagbok;

import org.joda.time.DateTime;

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
	}

	private void assertInitialsOnQuestions() {
		assertQuestionFullState(R.id.generallyWell, "Väsentligen frisk", true);
		assertQuestionFullState(R.id.malaise, "Sjukdomskänsla", false);
		assertQuestionFullState(R.id.fever, "Feber > 38", false);
		assertQuestionFullState(R.id.earAche, "Öronvärk", false);
		assertQuestionFullState(R.id.soreThroat, "Halsont", false);
		assertQuestionFullState(R.id.runnyNose, "Snuva", false);
		assertQuestionFullState(R.id.stommacAche, "Magbesvär", false);
		assertQuestionFullState(R.id.dryCough, "Torrhosta", false);
		assertQuestionFullState(R.id.wetCough, "Slemhosta", false);
		assertQuestionFullState(R.id.morningCough, "Morgonupphostning", false);
	}


	public void testClickingArrowNavigation() throws Exception {
		clickOnQuestionWithId(R.id.generallyWell); // Change someting so that later check does not compare with default

		WeekAnswers beforeModel = getActivity().model;

		assertFalse("beforeModel generallyWell should have changed", beforeModel.getAnswer(R.id.generallyWell));

		solo.clickOnView(solo.getView(R.id.previousWeek));

		WeekAnswers afterModel = getActivity().model;

		assertFalse("not same model", beforeModel == afterModel);
		assertFalse("not equal models", beforeModel.equals(afterModel));
		assertTrue("week before model", beforeModel.week.previous().equals(afterModel.week));

		solo.clickOnView(solo.getView(R.id.nextWeek));

		WeekAnswers nextModel = getActivity().model;

		assertEquals("same week again after going back and forward", beforeModel.week, nextModel.week);
		assertEquals("week answers equality when going back and forward", beforeModel, nextModel);
	}

}
