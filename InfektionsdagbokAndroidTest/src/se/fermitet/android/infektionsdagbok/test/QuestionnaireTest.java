package se.fermitet.android.infektionsdagbok.test;

import com.robotium.solo.Solo;

import se.fermitet.android.infektionsdagbok.Quesionnaire;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ToggleButton;

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
		
		checkToggleButton("malaise");
		checkToggleButton("fever");
		checkToggleButton("earAche");
		checkToggleButton("soreThroat");
		checkToggleButton("runnyNose");
		checkToggleButton("stommacAche");
		checkToggleButton("dryCough");
		checkToggleButton("wetCough");
		checkToggleButton("morningCough");
		checkToggleButton("generallyWell");
	}
	
	private void checkForText(String text)  {
		assertTrue("Should have text: " + text, solo.searchText(text));
	}
	
	private void checkToggleButton(String name) {
		View view = solo.getView("@id/" + name + "SCT");
		assertNotNull(view);
		
		assertTrue(name + " view should be a ToggleButton", view instanceof ToggleButton);
		ToggleButton tb = (ToggleButton) view;
		
		assertEquals(name + " toggle button off text", "Nej", tb.getTextOff().toString());
		assertEquals(name + " toggle button on text", "Ja", tb.getTextOn().toString());
	}

}

