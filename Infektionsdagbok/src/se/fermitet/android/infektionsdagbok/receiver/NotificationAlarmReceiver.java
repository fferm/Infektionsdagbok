package se.fermitet.android.infektionsdagbok.receiver;

import se.fermitet.android.infektionsdagbok.Questionnaire;
import se.fermitet.android.infektionsdagbok.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;

public class NotificationAlarmReceiver extends BroadcastReceiver {
	static int NOTIFICATION_ID = 1230942; // Actual value not interesting, it just needs to be a constant

	@Override
	public void onReceive(Context context, Intent intent) {
		issueNotification(context);
	}


	private void issueNotification(Context context) {
		NotificationManager notificationManager = getNotificationManager(context);

		notificationManager.notify(NOTIFICATION_ID, createNotification(context));
	}

	Notification createNotification(Context context) {
		NotificationCompat.Builder builder = createNotificationBuilder(context);
		builder.setSmallIcon(R.drawable.ic_notification);
		builder.setContentTitle("Infektionsdagbok");
		builder.setContentText("Fyll i veckans infektionsdagbok");

		Intent resultIntent = new Intent(context, Questionnaire.class);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(Questionnaire.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(resultPendingIntent);
		return builder.build();
	}


	protected NotificationManager getNotificationManager(Context context) {
		return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	protected Builder createNotificationBuilder(Context context) {
		return new NotificationCompat.Builder(context);
	}


}
