package se.fermitet.android.infektionsdagbok.test;

import se.fermitet.android.infektionsdagbok.Quesionnaire;
import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.views.QuestionView;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.robotium.solo.Solo;

public class QuestionnaireTest extends ActivityInstrumentationTestCase2<Quesionnaire> {

	public QuestionnaireTest() {
		super(Quesionnaire.class);
	}

	private Solo solo;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}

	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
		super.tearDown();
	}

	public void testInitialsOnQuestions() throws Exception {
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

	private void assertQuestionFullState(int id, String questionText, boolean checked, boolean enabled) {
		QuestionView view = (QuestionView) solo.getView(id);
		assertNotNull(view);

		assertText(id, questionText);
		assertChecked(id, checked);
		assertEnabled(id, enabled);
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
		Thread.sleep(500);


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


	private void assertText(int id, String text) {
		QuestionView view = (QuestionView) solo.getView(id);

		TextView tv = (TextView) view.findViewById(R.id.questionText);
		assertEquals(getNameFromId(id) + " question text", text, tv.getText());
	}

	private void assertChecked(int id, boolean checked) {
		QuestionView view = (QuestionView) solo.getView(id);

		CompoundButton selector =  (CompoundButton) view.findViewById(R.id.answerSelector);
		assertEquals(getNameFromId(id) + " selector has wrong state", checked, selector.isChecked());
	}

	private void assertEnabled(int id, boolean enabled) {
		QuestionView view = (QuestionView) solo.getView(id);

		CompoundButton selector =  (CompoundButton) view.findViewById(R.id.answerSelector);
		TextView tv = (TextView) view.findViewById(R.id.questionText);

		assertTrue(getNameFromId(id) + " text should be enabled", tv.isEnabled());
		assertEquals(getNameFromId(id) + " selector enabled value", enabled, selector.isEnabled());
	}

	private void clickOnCompoundButtonOfQuestionWithId(int id) {
		QuestionView questionView = (QuestionView) solo.getView(id);

		CompoundButton answer = (CompoundButton) questionView.findViewById(R.id.answerSelector);
		solo.clickOnView(answer);
	}

	private String getNameFromId(int id) {
		if (id == R.id.malaise) return "malaise";
		if (id == R.id.fever) return "fever";
		if (id == R.id.earAche) return "earAche";
		if (id == R.id.soreThroat) return "soreThroat";
		if (id == R.id.runnyNose) return "runnyNose";
		if (id == R.id.stommacAche) return "stommacAche";
		if (id == R.id.dryCough) return "dryCough";
		if (id == R.id.wetCough) return "wetCough";
		if (id == R.id.morningCough) return "morningCough";
		if (id == R.id.generallyWell) return "generallyWell";

		return "unknown";

	}
}

