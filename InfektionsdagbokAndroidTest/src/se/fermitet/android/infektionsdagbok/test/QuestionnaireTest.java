package se.fermitet.android.infektionsdagbok.test;

import se.fermitet.android.infektionsdagbok.Quesionnaire;
import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.views.QuestionView;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

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
	
	public void testHasQuestions() throws Exception {
		checkQuestion(R.id.malaise, "Sjukdomskänsla");
		checkQuestion(R.id.fever, "Feber > 38");
		checkQuestion(R.id.earAche, "Öronvärk");
		checkQuestion(R.id.soreThroat, "Halsont");
		checkQuestion(R.id.runnyNose, "Snuva");
		checkQuestion(R.id.stommacAche, "Magbesvär");
		checkQuestion(R.id.dryCough, "Torrhosta");
		checkQuestion(R.id.wetCough, "Slemhosta");
		checkQuestion(R.id.morningCough, "Morgonupphostning");
		checkQuestion(R.id.generallyWell, "Väsentligen frisk");
	}
	
	private void checkQuestion(int id, String questionText) {
		View view = solo.getView(id);
		assertNotNull(view);
		
		assertTrue(questionText + " view should be a QuestionView", view instanceof QuestionView);

	}
}

