package se.fermitet.android.infektionsdagbok;

import se.fermitet.android.infektionsdagbok.app.InfektionsdagbokApplication;
import se.fermitet.android.infektionsdagbok.helper.NameFromIdHelper;
import se.fermitet.android.infektionsdagbok.views.QuestionView;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.robotium.solo.Solo;

public abstract class QuestionnaireTest extends ActivityInstrumentationTestCase2<Questionnaire>{


	protected Solo solo;

	public QuestionnaireTest() {
		super(Questionnaire.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		onSetupBeforeActivityCreation();
		solo = new Solo(getInstrumentation(), getActivity());
	}

	protected void onSetupBeforeActivityCreation() {}

	@Override
	protected void tearDown() throws Exception {
		InfektionsdagbokApplication app = (InfektionsdagbokApplication) getActivity().getApplication();

		app.getModelManager().reset();

		solo.finishOpenedActivities();
		getActivity().finish();
		app.clear();

		super.tearDown();
	}

	protected void clickOnQuestionWithId(int id) {
		QuestionView questionView = (QuestionView) solo.getView(id);
		solo.clickOnView(questionView);
	}

	protected void assertQuestionFullState(int id, String questionText, boolean checked, boolean enabled) {
		QuestionView view = (QuestionView) solo.getView(id);
		assertNotNull(view);

		assertText(id, questionText);
		assertChecked(id, checked);
		assertEnabled(id, enabled);
	}

	protected void assertText(int id, String text) {
		QuestionView view = (QuestionView) solo.getView(id);

		TextView tv = (TextView) view.findViewById(R.id.questionText);
		assertEquals(NameFromIdHelper.getNameFromId(id) + " question text", text, tv.getText());
	}

	protected void assertChecked(int id, boolean shouldBeChecked) {
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

	protected void assertEnabled(int id, boolean enabled) {
		QuestionView view = (QuestionView) solo.getView(id);

		CompoundButton selector =  (CompoundButton) view.findViewById(R.id.answerSelector);
		TextView tv = (TextView) view.findViewById(R.id.questionText);

		assertTrue(NameFromIdHelper.getNameFromId(id) + " text should be enabled", tv.isEnabled());
		assertEquals(NameFromIdHelper.getNameFromId(id) + " selector enabled value", enabled, selector.isEnabled());
	}

	protected void assertClickingQuestionPart(int questionId, View viewToClick, String nameOfView, boolean shouldChange) {
		boolean before = getActivity().model.getAnswer(questionId);
		solo.clickOnView(viewToClick);
		boolean after = getActivity().model.getAnswer(questionId);


		if (shouldChange) {
			assertFalse("Should have changed after clicking " + nameOfView, before == after);
		} else {
			assertTrue("Should not have changed after clicking the disabled version of " + nameOfView, before == after);
		}
	}

}
