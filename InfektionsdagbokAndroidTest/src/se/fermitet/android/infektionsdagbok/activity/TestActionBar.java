package se.fermitet.android.infektionsdagbok.activity;

import se.fermitet.android.infektionsdagbok.R;
import android.app.ActionBar;
import android.app.Activity;

public abstract class TestActionBar<ACTIVITY extends Activity> extends ActivityTestWithSolo<ACTIVITY> {

	private Class<? extends Activity> startingClass;

	public TestActionBar(Class<ACTIVITY> startingClass) {
		super(startingClass);
		this.startingClass = startingClass;
	}

	public void testActionBar() throws Exception {
		ActionBar actionBar = getActivity().getActionBar();

		assertNotNull("not null", actionBar);
		assertTrue("showing", actionBar.isShowing());
		assertEquals("navigation mode", ActionBar.NAVIGATION_MODE_STANDARD, actionBar.getNavigationMode());
	}

	public void testActionBarForQuestionnaireActivity() throws Exception {
		checkForExport(Questionnaire.class, "Questionnaire", R.id.actionQuestionnaire, false);
	}

	public void testActionBarForExportActivity() throws Exception {
		checkForExport(ExportActivity.class, "Export", R.id.actionExport);
	}

	public void testActionBarForTreatmentActivity() throws Exception {
		checkForExport(TreatmentMasterActivity.class, "Treatment", R.id.actionTreatment);
	}

	public void testActionBarForSickDayActivity() throws Exception {
		checkForExport(SickDayMasterActivity.class, "SickDay", R.id.actionSickDay);
	}

	private void checkForExport(Class<?> activityToGoTo, String activityToGoToName, int actionId) throws Exception {
		checkForExport(activityToGoTo, activityToGoToName, actionId, true);
	}

	private void checkForExport(Class<?> activityToGoTo, String activityToGoToName, int actionId, boolean canGoHome) throws Exception {
		solo.clickOnActionBarItem(actionId);
		timeoutGetCurrentActivity(activityToGoTo);

		int options = solo.getCurrentActivity().getActionBar().getDisplayOptions();
		boolean homeAsUp = (options & ActionBar.DISPLAY_HOME_AS_UP) != 0;
		assertTrue("DISPLAY_HOME_AS_UP option should be set on Activity actionbar", homeAsUp == canGoHome);

		if (canGoHome) {
			solo.clickOnActionBarHomeButton();
			timeoutGetCurrentActivity(startingClass);
		}
	}


}
