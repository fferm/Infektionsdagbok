package se.fermitet.android.infektionsdagbok.activity;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.app.Factory;
import se.fermitet.android.infektionsdagbok.helper.NameFromIdHelper;
import se.fermitet.android.infektionsdagbok.views.QuestionView;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

public abstract class QuestionnaireTest extends ActivityTestWithSolo<Questionnaire>{

	public QuestionnaireTest() {
		this(null);
	}

	public QuestionnaireTest(Class<? extends Factory> mockedFactoryClassOrNull) {
		super(Questionnaire.class, mockedFactoryClassOrNull);
	}

	protected void clickOnQuestionWithId(int id) {
		QuestionView questionView = (QuestionView) solo.getView(id);
		solo.clickOnView(questionView);
	}

	protected void assertQuestionFullState(int id, String questionText, boolean checked) {
		QuestionView view = (QuestionView) solo.getView(id);
		assertNotNull(view);

		assertText(id, questionText);
		assertChecked(id, checked);
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
