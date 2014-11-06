package se.fermitet.android.infektionsdagbok.activity;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.app.InfektionsdagbokApplication;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;
import se.fermitet.android.infektionsdagbok.receiver.NotificationAlarmReceiver;
import se.fermitet.android.infektionsdagbok.views.QuestionnaireView;
import se.fermitet.android.infektionsdagbok.views.QuestionnaireView.OnWeekChangeListener;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

public class Questionnaire extends InfektionsdagbokActivity<QuestionnaireView> implements OnWeekChangeListener {

	WeekAnswers model;

	public Questionnaire() {
		super(R.layout.questionnaire_view);
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
			issueNotificationAlarm();
			view.setOnWeekChangeListener(this);

			setNewWeek(getLocalApplication().getModelManager().getInitialWeekAnswers());
		} catch (Exception e) {
			view.handleException(e);
		}
    }

	private void issueNotificationAlarm() {
		AlarmManager am = ((InfektionsdagbokApplication) getApplication()).getAlarmManager();

		long sundayAtSevenPM = new DateTime()
			.dayOfWeek().setCopy(DateTimeConstants.SUNDAY)
			.millisOfDay().setCopy(19 * 60 * 60 * 1000).getMillis();

		long week = 7 * 24 * 60 * 60 * 1000;

		Intent intent = new Intent(this, NotificationAlarmReceiver.class);
		PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		am.setRepeating(AlarmManager.RTC_WAKEUP, sundayAtSevenPM, week, pIntent);
	}

	@Override
	public void onWeekIncrement() {
		try {
			getLocalApplication().getVibrator().vibrate(75);
			setNewWeek(getLocalApplication().getModelManager().getNextWeekAnswers(this.model));
		} catch (Exception e) {
			view.handleException(e);
		}
	}


	@Override
	public void onWeekDecrement() {
		try {
			getLocalApplication().getVibrator().vibrate(75);
			setNewWeek(getLocalApplication().getModelManager().getPreviousWeekAnswers(this.model));
		} catch (Exception e) {
			view.handleException(e);
		}
	}

	private void setNewWeek(WeekAnswers newWeekAnswers) throws Exception {
		model = newWeekAnswers;
        view.setModel(newWeekAnswers);
	}
}
