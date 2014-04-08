package se.fermitet.android.infektionsdagbok;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import se.fermitet.android.infektionsdagbok.app.Factory;
import se.fermitet.android.infektionsdagbok.app.InfektionsdagbokApplication;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;
import se.fermitet.android.infektionsdagbok.receiver.NotificationAlarmReceiver;
import se.fermitet.android.infektionsdagbok.views.QuestionnaireView;
import se.fermitet.android.infektionsdagbok.views.QuestionnaireView.OnWeekChangeListener;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class Questionnaire extends Activity implements OnWeekChangeListener {

	public static final String FACTORY_KEY = "FACTORY_OBJECT";

	private QuestionnaireView view;

	WeekAnswers model;

	public Questionnaire() {
		super();
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setApplicationFactory();

        issueNotificationAlarm();

        view = (QuestionnaireView) View.inflate(this, R.layout.questionnaire_view, null);
        view.setOnWeekChangeListener(this);

        setNewWeek(getLocalApplication().getModelManager().getInitialWeekAnswers());

        setContentView(view);
    }

	private void setApplicationFactory() {
		Intent intent = getIntent();
		if (intent == null) return;

        Bundle extras = intent.getExtras();
        if (extras == null) return;

        Object obj = extras.get(FACTORY_KEY);
        if (obj != null && obj instanceof Factory) {
        	InfektionsdagbokApplication app = (InfektionsdagbokApplication) getApplication();
        	app.setFactory((Factory) obj);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quesionnaire, menu);
        return true;
    }

	@Override
	public void onWeekIncrement() {
		try {
			setNewWeek(getLocalApplication().getModelManager().getNextWeekAnswers(this.model));
		} catch (Exception e) {
			e.printStackTrace();
			notifyUserOfException(e);
		}
	}


	@Override
	public void onWeekDecrement() {
		try {
			setNewWeek(getLocalApplication().getModelManager().getPreviousWeekAnswers(this.model));
		} catch (Exception e) {
			e.printStackTrace();
			notifyUserOfException(e);
		}
	}

	private InfektionsdagbokApplication getLocalApplication() {
		return (InfektionsdagbokApplication) getApplication();
	}



	private void notifyUserOfException(Exception e) {
		Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
		toast.show();
	}


	private void setNewWeek(WeekAnswers newWeekAnswers) {
		model = newWeekAnswers;
        view.setModel(newWeekAnswers);
	}
}
