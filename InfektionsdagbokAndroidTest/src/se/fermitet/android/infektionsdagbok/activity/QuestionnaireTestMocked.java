package se.fermitet.android.infektionsdagbok.activity;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.app.Factory;
import se.fermitet.android.infektionsdagbok.app.InfektionsdagbokApplication;
import se.fermitet.android.infektionsdagbok.helper.NameFromIdHelper;
import se.fermitet.android.infektionsdagbok.model.Week;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;
import se.fermitet.android.infektionsdagbok.storage.Storage;
import se.fermitet.android.infektionsdagbok.test.MockedStorageFactory;
import se.fermitet.android.infektionsdagbok.views.QuestionView;
import se.fermitet.android.infektionsdagbok.views.QuestionnaireView;
import android.app.AlarmManager;
import android.app.Instrumentation;
import android.app.PendingIntent;
import android.os.Vibrator;
import android.view.View;

public class QuestionnaireTestMocked extends QuestionnaireTest {

	public QuestionnaireTestMocked() {
		super(MockedStorageFactory.class);
	}

	@Override
	protected void tearDown() throws Exception {
		InfektionsdagbokApplication app = (InfektionsdagbokApplication) getActivity().getApplication();

		reset(app.getStorage());

		super.tearDown();
	}

	public void testFactoryInjection() throws Exception {
		InfektionsdagbokApplication app = (InfektionsdagbokApplication) getActivity().getApplication();
		Factory factory = app.getFactory();
		assertEquals("factory class", MockedStorageFactory.class, factory.getClass());
	}

	public void testClickingAnswersChangesModel() throws Exception {
		WeekAnswers model = getActivity().model;

		for (Integer idObj : WeekAnswers.questionIds) {
			int id = idObj.intValue();
			boolean before = model.getAnswer(id);

			clickOnQuestionWithId(id);

			boolean after;

			setStart();
			do {
				after = model.getAnswer(id);

				setElapsed();
			} while (after == before && notYetTimeout());

			assertTrue(NameFromIdHelper.getNameFromId(id) + " after", before != model.getAnswer(id));
		}
	}


	public void testClickingAllQuestionPartsChangesAnswer() throws Exception {
		int questionId = R.id.malaise;
		QuestionView questionView = (QuestionView) solo.getView(questionId);
		View selector = questionView.findViewById(R.id.answerSelector);
		View text = questionView.findViewById(R.id.questionText);

		assertClickingQuestionPartWithTimeout(questionId, selector, "selector", true);
		assertClickingQuestionPartWithTimeout(questionId, text, "text view", true);
		assertClickingQuestionPartWithTimeout(questionId, questionView, "full question", true);

	}

	public void testSaveToStorage() throws Exception {
		Questionnaire questionnaire = getActivity();
		WeekAnswers model = questionnaire.model;

		InfektionsdagbokApplication app =  (InfektionsdagbokApplication) questionnaire.getApplication();
		Storage storage = app.getStorage();

		solo.clickOnView(solo.getView(R.id.nextWeek));
		verify(storage, timeout((int) TIMEOUT)).saveAnswers(model);
		reset(storage);

		model = questionnaire.model;
		solo.clickOnView(solo.getView(R.id.previousWeek));
		verify(storage, timeout((int) TIMEOUT)).saveAnswers(model);
		reset(storage);

		model = questionnaire.model;
		Instrumentation ins = getInstrumentation();
		ins.callActivityOnPause(questionnaire);
		solo.clickOnView(solo.getView(R.id.previousWeek));
		verify(storage, timeout((int) TIMEOUT)).saveAnswers(model);
		reset(storage);

		app.getModelManager().reset();
		verify(storage, timeout((int) TIMEOUT)).clear();
	}

	public void testReadFromStorage() throws Exception {
		InfektionsdagbokActivity<QuestionnaireView> questionnaire = getActivity();
		InfektionsdagbokApplication app = (InfektionsdagbokApplication) questionnaire.getApplication();
		Storage storage = app.getStorage();
		reset(storage);

		// Back and forward to get back to original week
		solo.clickOnView(solo.getView(R.id.nextWeek));
		solo.clickOnView(solo.getView(R.id.previousWeek));
		Thread.sleep(1000);

		verify(storage, timeout((int) TIMEOUT)).getAnswersForWeek(new Week(new LocalDate()));
	}

	public void testAlarmForNotificationIsSet() throws Exception {
		AlarmManager mgr = getActivity().getLocalApplication().getAlarmManager();

		DateTime startInstant = new DateTime();
		startInstant = startInstant.dayOfWeek().setCopy(DateTimeConstants.SUNDAY);
		startInstant = startInstant.millisOfDay().setCopy(19 * 60 * 60 * 1000);

		long week = 7 * 24 * 60 * 60 * 1000;

		// I was unable to check the intent...
		verify(mgr, timeout((int) TIMEOUT)).setRepeating(eq(AlarmManager.RTC_WAKEUP), eq(startInstant.getMillis()), eq(week), (PendingIntent) isNotNull());
	}

	public void testVibrateWhenChangingWeeks() throws Exception {
		Vibrator vib = getActivity().getLocalApplication().getVibrator();

		solo.clickOnView(solo.getView(R.id.nextWeek));
		verify(vib, timeout((int) TIMEOUT)).vibrate(anyInt());

		reset(vib);

		solo.clickOnView(solo.getView(R.id.previousWeek));
		verify(vib, timeout((int) TIMEOUT)).vibrate(anyInt());
	}
}

