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
		checkQuestion(R.id.malaise, "Sjukdomskänsla", false);
		checkQuestion(R.id.fever, "Feber > 38", false);
		checkQuestion(R.id.earAche, "Öronvärk", false);
		checkQuestion(R.id.soreThroat, "Halsont", false);
		checkQuestion(R.id.runnyNose, "Snuva", false);
		checkQuestion(R.id.stommacAche, "Magbesvär", false);
		checkQuestion(R.id.dryCough, "Torrhosta", false);
		checkQuestion(R.id.wetCough, "Slemhosta", false);
		checkQuestion(R.id.morningCough, "Morgonupphostning", false);
		checkQuestion(R.id.generallyWell, "Väsentligen frisk", true);
	}

	private void checkQuestion(int id, String questionText, boolean checked) {
		QuestionView view = (QuestionView) solo.getView(id);
		assertNotNull(view);

		TextView tv = (TextView) view.findViewById(R.id.questionText);
		assertEquals(getNameFromId(id) + " question text", questionText, tv.getText());

		CompoundButton selector =  (CompoundButton) view.findViewById(R.id.answerSelector);
		assertEquals(getNameFromId(id) + " selector has wrong state", checked, selector.isChecked());
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

