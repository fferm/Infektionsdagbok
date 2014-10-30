package se.fermitet.android.infektionsdagbok.activity;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.views.QuestionnaireView;
import android.app.ActionBar;
import android.app.Activity;

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
		solo.clickOnActionBarItem(R.id.actionExport);
		assertTrue("Export activity started", solo.waitForActivity(ExportActivity.class));

		int options = solo.getCurrentActivity().getActionBar().getDisplayOptions();
		assertTrue("DISPLAY_HOME_AS_UP option should be set on Activity actionbar", (options & ActionBar.DISPLAY_HOME_AS_UP) != 0);

		solo.clickOnActionBarHomeButton();

		Activity currentActivity = timeoutGetCurrentActivity(Questionnaire.class);
		assertEquals("Should be back to Questionnaire after back click", Questionnaire.class,  currentActivity.getClass());
	}

	public void testActionBarForTreatmentActivity() throws Exception {
		solo.clickOnActionBarItem(R.id.actionTreatment);
		assertTrue("Treatment activity started", solo.waitForActivity(TreatmentMasterActivity.class));

		int options = solo.getCurrentActivity().getActionBar().getDisplayOptions();
		assertTrue("DISPLAY_HOME_AS_UP option should be set on Activity actionbar", (options & ActionBar.DISPLAY_HOME_AS_UP) != 0);

		solo.clickOnActionBarHomeButton();

		Activity currentActivity = timeoutGetCurrentActivity(Questionnaire.class);
		assertEquals("Should be back to Questionnaire after back click", Questionnaire.class,  currentActivity.getClass());
	}

	public void testActionBarForSickDayActivity() throws Exception {
		solo.clickOnActionBarItem(R.id.actionSickDay);
		assertTrue("Sick day activity started", solo.waitForActivity(SickDayMasterActivity.class));

		int options = solo.getCurrentActivity().getActionBar().getDisplayOptions();
		assertTrue("DISPLAY_HOME_AS_UP option should be set on Activity actionbar", (options & ActionBar.DISPLAY_HOME_AS_UP) != 0);

		solo.clickOnActionBarHomeButton();

		Activity currentActivity = timeoutGetCurrentActivity(Questionnaire.class);
		assertEquals("Should be back to Questionnaire after back click", Questionnaire.class,  currentActivity.getClass());
	}

}
