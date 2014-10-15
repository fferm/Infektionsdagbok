package se.fermitet.android.infektionsdagbok.activity;

import se.fermitet.android.infektionsdagbok.app.Factory;
import se.fermitet.android.infektionsdagbok.app.InfektionsdagbokApplication;
import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

public class ActivityTestWithSolo<T extends Activity> extends ActivityInstrumentationTestCase2<T>{

	protected Solo solo;

	private Class<? extends Factory> mockedFactoryClassOrNull;

	public ActivityTestWithSolo(Class<T> activityClass) {
		this(activityClass, null);
	}

	public ActivityTestWithSolo(Class<T> activityClass, Class<? extends Factory> mockedFactoryClassOrNull) {
		super(activityClass);
		this.mockedFactoryClassOrNull = mockedFactoryClassOrNull;
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		onBeforeActivityCreation();

		handleFactory();

		solo = new Solo(getInstrumentation(), getActivity());
	}

	protected void onBeforeActivityCreation() throws Exception {}


	private void handleFactory() {
		if (mockedFactoryClassOrNull != null) {
			Intent i = new Intent();
			i.putExtra(InfektionsdagbokActivity.FACTORY_KEY, mockedFactoryClassOrNull.getName());

			super.setActivityIntent(i);

		}
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
