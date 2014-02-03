package se.fermitet.android.infektionsdagbok;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.helper.NameFromIdHelper;
import se.fermitet.android.infektionsdagbok.model.ModelManager;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;
import se.fermitet.android.infektionsdagbok.views.QuestionView;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.robotium.solo.Solo;

public class QuestionnaireTest extends ActivityInstrumentationTestCase2<Questionnaire> {

	private List<Integer> questionIds;
	private Solo solo;

	public QuestionnaireTest() {
		super(Questionnaire.class);
		initQuestionIds();
	}

	private void initQuestionIds() {
		questionIds = new ArrayList<Integer>();

		questionIds.add(R.id.generallyWell);
		questionIds.add(R.id.malaise);
		questionIds.add(R.id.fever);
		questionIds.add(R.id.earAche);
		questionIds.add(R.id.soreThroat);
		questionIds.add(R.id.runnyNose);
		questionIds.add(R.id.stommacAche);
		questionIds.add(R.id.dryCough);
		questionIds.add(R.id.wetCough);
		questionIds.add(R.id.morningCough);
	}


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

	public void testInitials() throws Exception {
		assertInitialsOnQuestions();

		TextView weekView = (TextView) solo.getView(R.id.weekDisplay);
		DateTime now = new DateTime();
		assertEquals("week text", "" + now.getYear() + "-" + now.getWeekOfWeekyear(), weekView.getText());
	}

	private void assertInitialsOnQuestions() {
		assertQuestionFullState(R.id.generallyWell, "VŠsentligen frisk", true, true);
		assertQuestionFullState(R.id.malaise, "SjukdomskŠnsla", false, false);
		assertQuestionFullState(R.id.fever, "Feber > 38", false, false);
		assertQuestionFullState(R.id.earAche, "…ronvŠrk", false, false);
		assertQuestionFullState(R.id.soreThroat, "Halsont", false, false);
		assertQuestionFullState(R.id.runnyNose, "Snuva", false, false);
		assertQuestionFullState(R.id.stommacAche, "MagbesvŠr", false, false);
		assertQuestionFullState(R.id.dryCough, "Torrhosta", false, false);
		assertQuestionFullState(R.id.wetCough, "Slemhosta", false, false);
		assertQuestionFullState(R.id.morningCough, "Morgonupphostning", false, false);
	}

	public void testStatusOfGenerallyWellChangesEnabledStateOfOthers() throws Exception {
		clickOnQuestionWithId(R.id.generallyWell);

		for (Integer idObj : questionIds) {
			int id = idObj.intValue();
			assertEnabled(id, true);
		}
		assertChecked(R.id.generallyWell, false);

		clickOnQuestionWithId(R.id.dryCough); // Change some state
		clickOnQuestionWithId(R.id.generallyWell);

		for (Integer idObj : questionIds) {
			int id = idObj.intValue();

			assertEnabled(id,  id == R.id.generallyWell);
		}

		assertChecked(R.id.dryCough, true);
		assertChecked(R.id.generallyWell, true);
	}

	public void testClickingAnswersChangesModel() throws Exception {
		WeekAnswers model = getActivity().model;

		for (Integer idObj : questionIds) {
			int id = idObj.intValue();
			assertClickingOnOneAnswer(model,  id);
		}
	}


	private void assertClickingOnOneAnswer(WeekAnswers wa, int id) {
		boolean before = wa.getAnswer(id);

		clickOnQuestionWithId(id);
		assertTrue(NameFromIdHelper.getNameFromId(id) + " after", before != wa.getAnswer(id));
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

	public void testClickingAllQuestionPartsChangesAnswer() throws Exception {
		int questionId = R.id.generallyWell;
		QuestionView questionView = (QuestionView) solo.getView(questionId);
		View selector = questionView.findViewById(R.id.answerSelector);
		View text = questionView.findViewById(R.id.questionText);

		assertClickingQuestionPart(questionId, selector, "selector", true);
		assertClickingQuestionPart(questionId, text, "text view", true);
		assertClickingQuestionPart(questionId, questionView, "full question", true);

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

	private void assertClickingQuestionPart(int questionId, View viewToClick, String nameOfView, boolean shouldChange) {
		boolean before = getActivity().model.getAnswer(questionId);
		solo.clickOnView(viewToClick);
		boolean after = getActivity().model.getAnswer(questionId);


		if (shouldChange) {
			assertFalse("Should have changed after clicking " + nameOfView, before == after);
		} else {
			assertTrue("Should not have changed after clicking the disabled version of " + nameOfView, before == after);
		}
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

	private void assertChecked(int id, boolean shouldBeChecked) {
		QuestionView view = (QuestionView) solo.getView(id);

		CompoundButton selector =  (CompoundButton) view.findViewById(R.id.answerSelector);
		assertEquals(NameFromIdHelper.getNameFromId(id) + " selector has wrong state", shouldBeChecked, selector.isChecked());

		if (shouldBeChecked) {
			Drawable background = view.getBackground();
			assertTrue(NameFromIdHelper.getNameFromId(id) + " should have a ColorDrawable background", background instanceof ColorDrawable);
			ColorDrawable colorBackground = (ColorDrawable) background;
			assertTrue(NameFromIdHelper.getNameFromId(id) +  "should have a non alpha-null background", colorBackground.getAlpha() > 0);
		} else {
			assertNull(NameFromIdHelper.getNameFromId(id) + " should not have a background", view.getBackground());
		}
	}

	private void assertEnabled(int id, boolean enabled) {
		QuestionView view = (QuestionView) solo.getView(id);

		CompoundButton selector =  (CompoundButton) view.findViewById(R.id.answerSelector);
		TextView tv = (TextView) view.findViewById(R.id.questionText);

		assertTrue(NameFromIdHelper.getNameFromId(id) + " text should be enabled", tv.isEnabled());
		assertEquals(NameFromIdHelper.getNameFromId(id) + " selector enabled value", enabled, selector.isEnabled());
	}

	private void clickOnQuestionWithId(int id) {
		QuestionView questionView = (QuestionView) solo.getView(id);
		solo.clickOnView(questionView);
	}
}

