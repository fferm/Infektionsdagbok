package se.fermitet.android.infektionsdagbok;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.model.WeekAnswers;
import se.fermitet.android.infektionsdagbok.views.QuestionView;
import android.view.View;
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
		assertQuestionFullState(R.id.generallyWell, "Väsentligen frisk", true, true);
		assertQuestionFullState(R.id.malaise, "Sjukdomskänsla", false, false);
		assertQuestionFullState(R.id.fever, "Feber > 38", false, false);
		assertQuestionFullState(R.id.earAche, "Öronvärk", false, false);
		assertQuestionFullState(R.id.soreThroat, "Halsont", false, false);
		assertQuestionFullState(R.id.runnyNose, "Snuva", false, false);
		assertQuestionFullState(R.id.stommacAche, "Magbesvär", false, false);
		assertQuestionFullState(R.id.dryCough, "Torrhosta", false, false);
		assertQuestionFullState(R.id.wetCough, "Slemhosta", false, false);
		assertQuestionFullState(R.id.morningCough, "Morgonupphostning", false, false);
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

	public void testClickingDisabledQuestionsDoesNotChangeAnswer() throws Exception {
		int questionId = R.id.malaise;
		QuestionView questionView = (QuestionView) solo.getView(questionId);
		View selector = questionView.findViewById(R.id.answerSelector);
		View text = questionView.findViewById(R.id.questionText);

		assertFalse("Should be disabled for this test", questionView.isEnabled());

		assertClickingQuestionPart(questionId, selector, "selector", false);
		assertClickingQuestionPart(questionId, text, "text view", false);
		assertClickingQuestionPart(questionId, questionView, "full question", false);
	}

}
