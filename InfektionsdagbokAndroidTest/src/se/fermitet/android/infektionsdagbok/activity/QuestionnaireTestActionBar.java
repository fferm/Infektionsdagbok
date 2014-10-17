package se.fermitet.android.infektionsdagbok.activity;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.views.QuestionnaireView;
import android.app.ActionBar;

public class QuestionnaireTestActionBar extends ActivityTestWithSolo<Questionnaire> {

	public QuestionnaireTestActionBar() {
		super(Questionnaire.class);
	}
	
	public void testActionBar() throws Exception {
		InfektionsdagbokActivity<QuestionnaireView> questionnaire = getActivity();

		ActionBar actionBar = questionnaire.getActionBar();

		assertNotNull("not null", actionBar);
		assertTrue("showing", actionBar.isShowing());
		assertEquals("navigation mode", ActionBar.NAVIGATION_MODE_STANDARD, actionBar.getNavigationMode());
	}
	
	public void testActionBarForExportActivity() throws Exception {
		solo.searchText("Export");

		solo.clickOnActionBarItem(R.id.actionExport);
		assertTrue("Export activity started", solo.waitForActivity(ExportActivity.class));

		int options = solo.getCurrentActivity().getActionBar().getDisplayOptions();
		assertTrue("DISPLAY_HOME_AS_UP option should be set on Activity actionbar", (options & ActionBar.DISPLAY_HOME_AS_UP) != 0);

		solo.clickOnActionBarHomeButton();
		Thread.sleep(500); // Sleep to let navigation happen
		assertEquals("Should be back to Questionnaire after back click", Questionnaire.class,  solo.getCurrentActivity().getClass());
	}
	
	public void testActionBarForTreatmentActivity() throws Exception {
		solo.searchText("Behandling");
		
		solo.clickOnActionBarItem(R.id.actionTreatment);
/*		assertTrue("Treatment activity started", solo.waitForActivity(TreatmentActivity.class));
		
		int options = solo.getCurrentActivity().getActionBar().getDisplayOptions();
		assertTrue("DISPLAY_HOME_AS_UP option should be set on Activity actionbar", (options & ActionBar.DISPLAY_HOME_AS_UP) != 0);

		solo.clickOnActionBarHomeButton();
		Thread.sleep(500); // Sleep to let navigation happen
		assertEquals("Should be back to Questionnaire after back click", Questionnaire.class,  solo.getCurrentActivity().getClass());
*/
	}
}
