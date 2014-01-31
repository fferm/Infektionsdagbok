package se.fermitet.android.infektionsdagbok;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.model.ModelManager;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;
import se.fermitet.android.infektionsdagbok.views.QuestionView;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.robotium.solo.Solo;

public class QuestionnaireTest extends ActivityInstrumentationTestCase2<Questionnaire> {

	public QuestionnaireTest() {
		super(Questionnaire.class);
	}

	private Solo solo;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}

	@Override
	protected void tearDown() throws Exception {
		ModelManager.instance().reset();
		solo.finishOpenedActivities();
		super.tearDown();
	}

	/*
	 * There seems to be some timing and threading issues for the first test that runs.
	 * Therefore this will wait one second to give time for everything to be set up properly
	 * This test should be the first one run
	 */
	public void test0001FirstTest() throws Exception {
		Thread.sleep(1000);
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

	public void testStatusOfGenerallyWellChangesEnabledStateOfOthers() throws Exception {
		clickOnCompoundButtonOfQuestionWithId(R.id.generallyWell);

		assertEnabled(R.id.malaise, true);
		assertEnabled(R.id.fever, true);
		assertEnabled(R.id.earAche, true);
		assertEnabled(R.id.soreThroat, true);
		assertEnabled(R.id.runnyNose, true);
		assertEnabled(R.id.stommacAche, true);
		assertEnabled(R.id.dryCough, true);
		assertEnabled(R.id.wetCough, true);
		assertEnabled(R.id.morningCough, true);
		assertEnabled(R.id.generallyWell, true);
		assertChecked(R.id.generallyWell, false);

		clickOnCompoundButtonOfQuestionWithId(R.id.dryCough); // Change some state
		clickOnCompoundButtonOfQuestionWithId(R.id.generallyWell);


		assertEnabled(R.id.malaise, false);
		assertEnabled(R.id.fever, false);
		assertEnabled(R.id.earAche, false);
		assertEnabled(R.id.soreThroat, false);
		assertEnabled(R.id.runnyNose, false);
		assertEnabled(R.id.stommacAche, false);
		assertEnabled(R.id.dryCough, false);
		assertEnabled(R.id.wetCough, false);
		assertEnabled(R.id.morningCough, false);
		assertEnabled(R.id.generallyWell, true);

		assertChecked(R.id.dryCough, true);
		assertChecked(R.id.generallyWell, true);
	}

	public void testClickingAnswersChangesModel() throws Exception {
		WeekAnswers model = getActivity().model;

		assertClickingOnOneAnswer(model, R.id.generallyWell, true);
		assertClickingOnOneAnswer(model, R.id.malaise, false);
		assertClickingOnOneAnswer(model, R.id.fever, false);
		assertClickingOnOneAnswer(model, R.id.earAche, false);
		assertClickingOnOneAnswer(model, R.id.soreThroat, false);
		assertClickingOnOneAnswer(model, R.id.runnyNose, false);
		assertClickingOnOneAnswer(model, R.id.stommacAche, false);
		assertClickingOnOneAnswer(model, R.id.dryCough, false);
		assertClickingOnOneAnswer(model, R.id.wetCough, false);
		assertClickingOnOneAnswer(model, R.id.morningCough, false);
	}


	private void assertClickingOnOneAnswer(WeekAnswers wa, int id, boolean before) {
		assertTrue(NameFromIdHelper.getNameFromId(id) + " before", before == wa.getAnswer(id));
		clickOnCompoundButtonOfQuestionWithId(id);
		assertTrue(NameFromIdHelper.getNameFromId(id) + " after", before != wa.getAnswer(id));
	}

	public void testClickingArrowNavigation() throws Exception {
		clickOnCompoundButtonOfQuestionWithId(R.id.generallyWell); // Change someting so that later check does not compare with default

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


	private void assertQuestionFullState(int id, String questionText, boolean checked, boolean enabled) {
		QuestionView view = (QuestionView) solo.getView(id);
		assertNotNull(view);

		assertText(id, questionText);
		assertChecked(id, checked);
		assertEnabled(id, enabled);
	}

	private void assertText(int id, String text) {
		QuestionView view = (QuestionView) solo.getView(id);

		TextView tv = (TextView) view.findViewById(R.id.questionText);
		assertEquals(NameFromIdHelper.getNameFromId(id) + " question text", text, tv.getText());
	}

	private void assertChecked(int id, boolean checked) {
		QuestionView view = (QuestionView) solo.getView(id);

		CompoundButton selector =  (CompoundButton) view.findViewById(R.id.answerSelector);
		assertEquals(NameFromIdHelper.getNameFromId(id) + " selector has wrong state", checked, selector.isChecked());
	}

	private void assertEnabled(int id, boolean enabled) {
		QuestionView view = (QuestionView) solo.getView(id);

		CompoundButton selector =  (CompoundButton) view.findViewById(R.id.answerSelector);
		TextView tv = (TextView) view.findViewById(R.id.questionText);

		assertTrue(NameFromIdHelper.getNameFromId(id) + " text should be enabled", tv.isEnabled());
		assertEquals(NameFromIdHelper.getNameFromId(id) + " selector enabled value", enabled, selector.isEnabled());
	}

	private void clickOnCompoundButtonOfQuestionWithId(int id) {
		QuestionView questionView = (QuestionView) solo.getView(id);

		CompoundButton answer = (CompoundButton) questionView.findViewById(R.id.answerSelector);
		solo.clickOnView(answer);
	}
}

