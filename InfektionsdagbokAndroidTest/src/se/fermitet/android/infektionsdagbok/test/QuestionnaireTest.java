package se.fermitet.android.infektionsdagbok.test;

import com.robotium.solo.Solo;

import se.fermitet.android.infektionsdagbok.Quesionnaire;
import android.test.ActivityInstrumentationTestCase2;

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
		checkForText("Sjukdomsk�nsla");
		checkForText("Feber > 38");
		checkForText("�ronv�rk");
		checkForText("Halsont");
		checkForText("Snuva");
		checkForText("Magbesv�r");
		checkForText("Torrhosta");
		checkForText("Slemhosta");
		checkForText("Morgonupphostning");
		checkForText("V�sentligen frisk");
	}
	
	private void checkForText(String text)  {
		assertTrue("Should have text: " + text, solo.searchText(text));
	}
}
