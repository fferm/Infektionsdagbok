package se.fermitet.android.infektionsdagbok.activity;

import se.fermitet.android.infektionsdagbok.Questionnaire;
import se.fermitet.android.infektionsdagbok.R;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;

public class NotificationStarter extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.notification_starter);
	}

	public void buttonPressed(View v) {
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

		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		int myId = 231231;
		notificationManager.notify(myId, builder.build());
	}
}
