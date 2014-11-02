package se.fermitet.android.infektionsdagbok.activity;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.app.Factory;
import se.fermitet.android.infektionsdagbok.app.InfektionsdagbokApplication;
import se.fermitet.android.infektionsdagbok.test.DoNotHandleExceptionsFactory;
import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;
import com.robotium.solo.Solo.Config;

public class ActivityTestWithSolo<T extends Activity> extends ActivityInstrumentationTestCase2<T>{

	protected Solo solo;

	private Class<? extends Factory> mockedFactoryClassOrNull;

	protected static long TIMEOUT = 5000;
	protected long start, elapsed;

	public ActivityTestWithSolo(Class<T> activityClass) {
		this(activityClass, DoNotHandleExceptionsFactory.class);
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

		Config conf = new Config();
		solo = new Solo(getInstrumentation(), conf, getActivity());
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
		app.clear();

		super.tearDown();
	}

	protected void setStart() {
		start = DateTime.now().getMillis();
	}

	protected void setElapsed() {
		elapsed = DateTime.now().getMillis() - start;
	}

	protected boolean notYetTimeout() {
		return elapsed < TIMEOUT;
	}

	protected Activity timeoutGetCurrentActivity(Class<?> expectedActivityClass) throws Exception {
		Activity currentActivity = null;
		setStart();
		do {
			currentActivity = solo.getCurrentActivity();
			setElapsed();
		} while (!currentActivity.getClass().equals(expectedActivityClass) && notYetTimeout());
		assertEquals("Wrong activity class", expectedActivityClass, currentActivity.getClass());

		return currentActivity;
	}

	public void assertBothNullOrEqual(String message, Object expected, Object actual) {
		if (expected == null) assertNull(message, actual);
		else assertEquals(message,  expected, actual);
	}




}
