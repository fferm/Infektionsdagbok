package se.fermitet.android.infektionsdagbok.receiver;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import junit.framework.TestCase;
import se.fermitet.android.infektionsdagbok.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.test.mock.MockContext;

public class NotificationAlarmReceiverTest extends TestCase {
	private MyContext testContext;

	@Override
	protected void setUp() throws Exception {
		testContext = new MyContext();
	}

	public void testNotificationContents() throws Exception {
		NotificationAlarmReceiverUnderTest receiver = new NotificationAlarmReceiverUnderTest();

		receiver.createNotification(testContext);

		verify(receiver.builder).setSmallIcon(R.drawable.ic_notification);
		verify(receiver.builder).setContentTitle("Infektionsdagbok");
		verify(receiver.builder).setContentText("Fyll i veckans infektionsdagbok");
		verify(receiver.builder).setContentIntent(any(PendingIntent.class));
		verify(receiver.builder).build();
	}

	public void testNotificationIsIssued() throws Exception {
		NotificationAlarmReceiverUnderTest receiver = new NotificationAlarmReceiverUnderTest();

		receiver.onReceive(testContext, null);

		verify(receiver.notificationManager).notify(eq(NotificationAlarmReceiver.NOTIFICATION_ID), any(Notification.class));
	}
}

class NotificationAlarmReceiverUnderTest extends NotificationAlarmReceiver {
	Builder builder = null;
	NotificationManager notificationManager = null;

	@Override
	protected Builder createNotificationBuilder(Context context) {
		builder = mock(NotificationCompat.Builder.class);
		return builder;
	}

	@Override
	protected NotificationManager getNotificationManager(Context context) {
		notificationManager = mock(NotificationManager.class);
		return notificationManager;
	}
}

class MyContext extends MockContext {
	private PackageManager packageManager;
	private ActivityInfo activityInfo;
	private ContentResolver contentResolver;

	public MyContext() {
		super();
		try {
			initMocks();
		} catch (NameNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private void initMocks() throws NameNotFoundException {
		packageManager = mock(PackageManager.class);
		activityInfo = mock(ActivityInfo.class);
		contentResolver = mock(ContentResolver.class);

		when(packageManager.getActivityInfo(any(ComponentName.class), anyInt())).thenReturn(activityInfo);

	}
	@Override
	public String getPackageName() {
		return "se.fermitet.android.infektionsdagbok";
	}

	@Override
	public PackageManager getPackageManager() {
		return this.packageManager;
	}

	@Override
	public ContentResolver getContentResolver() {
		return this.contentResolver;
	}
}
