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
	
	public void testCorrectActivity() throws Exception {
		solo.assertCurrentActivity("Wrong activity", Quesionnaire.class);
	}
}
