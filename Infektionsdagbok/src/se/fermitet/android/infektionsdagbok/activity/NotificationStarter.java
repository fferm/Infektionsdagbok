package se.fermitet.android.infektionsdagbok.activity;

import se.fermitet.android.infektionsdagbok.Questionnaire;
import se.fermitet.android.infektionsdagbok.R;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;

public class NotificationStarter extends Activity {

	private int notificationId = 12345;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.notification_starter);
	}

	public void buttonPressed(View v) {
		NotificationManager notificationManager = getNotificationManager();

		notificationManager.notify(notificationId, createNotification());
	}

	public void removePressed(View v) {
		NotificationManager notificationManager = getNotificationManager();
		notificationManager.cancel(notificationId);
	}

	private NotificationManager getNotificationManager() {
		return (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	}


	private Notification createNotification() {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setSmallIcon(R.drawable.ic_notification);
		builder.setContentTitle("Infektionsdagbok");
		builder.setContentText("Fyll i veckans infektionsdagbok");

		Intent resultIntent = new Intent(this, Questionnaire.class);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(Questionnaire.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(resultPendingIntent);
		return builder.build();
	}
}
