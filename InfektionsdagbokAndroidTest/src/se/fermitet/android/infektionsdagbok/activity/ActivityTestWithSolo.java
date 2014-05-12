package se.fermitet.android.infektionsdagbok.activity;

import se.fermitet.android.infektionsdagbok.app.InfektionsdagbokApplication;
import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

public class ActivityTestWithSolo<T extends Activity> extends ActivityInstrumentationTestCase2<T>{

	protected Solo solo;

	public ActivityTestWithSolo(Class<T> activityClass) {
		super(activityClass);
	}

	protected void onSetupBeforeActivityCreation() {}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		onSetupBeforeActivityCreation();
		solo = new Solo(getInstrumentation(), getActivity());
	}

	@Override
	protected void tearDown() throws Exception {
		InfektionsdagbokApplication app = (InfektionsdagbokApplication) getActivity().getApplication();

		app.getModelManager().reset();

		solo.finishOpenedActivities();
		getActivity().finish();
		app.clear();

		super.tearDown();
	}
}
