package se.fermitet.android.infektionsdagbok.activity;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;


public class NotificationTest extends ActivityInstrumentationTestCase2<NotificationStarter>{

	private Solo solo;

	public NotificationTest() {
		super(NotificationStarter.class);
	}

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

	public void testHasButtonToAddNotification() throws Exception {
		assertTrue(solo.searchButton("Add notification"));
	}

}
