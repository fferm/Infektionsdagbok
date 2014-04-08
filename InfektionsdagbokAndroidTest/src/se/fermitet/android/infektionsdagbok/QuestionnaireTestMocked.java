package se.fermitet.android.infektionsdagbok;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import se.fermitet.android.infektionsdagbok.app.Factory;
import se.fermitet.android.infektionsdagbok.app.InfektionsdagbokApplication;
import se.fermitet.android.infektionsdagbok.helper.NameFromIdHelper;
import se.fermitet.android.infektionsdagbok.model.ModelManager;
import se.fermitet.android.infektionsdagbok.model.Week;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;
import se.fermitet.android.infektionsdagbok.storage.Storage;
import se.fermitet.android.infektionsdagbok.test.MockedStorageFactory;
import se.fermitet.android.infektionsdagbok.views.QuestionView;
import android.app.AlarmManager;
import android.app.Instrumentation;
import android.app.PendingIntent;
import android.content.Intent;
import android.view.View;

public class QuestionnaireTestMocked extends QuestionnaireTest {

	public QuestionnaireTestMocked() {
		super();
	}

	@Override
	protected void onSetupBeforeActivityCreation() {
		Intent i = new Intent();
		i.putExtra(Questionnaire.FACTORY_KEY, new MockedStorageFactory());

		super.setActivityIntent(i);
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
			assertTrue(NameFromIdHelper.getNameFromId(id) + " after", before != model.getAnswer(id));
		}
	}


	public void testClickingAllQuestionPartsChangesAnswer() throws Exception {
		int questionId = R.id.malaise;
		QuestionView questionView = (QuestionView) solo.getView(questionId);
		View selector = questionView.findViewById(R.id.answerSelector);
		View text = questionView.findViewById(R.id.questionText);

		assertClickingQuestionPart(questionId, selector, "selector", true);
		assertClickingQuestionPart(questionId, text, "text view", true);
		assertClickingQuestionPart(questionId, questionView, "full question", true);

	}

	public void testSaveToStorage() throws Exception {
		Questionnaire questionnaire = getActivity();
		WeekAnswers model = questionnaire.model;

		InfektionsdagbokApplication app =  (InfektionsdagbokApplication) questionnaire.getApplication();
		Storage storage = app.getStorage();

		solo.clickOnView(solo.getView(R.id.nextWeek));
		verify(storage).saveAnswers(model);
		reset(storage);

		model = questionnaire.model;
		solo.clickOnView(solo.getView(R.id.previousWeek));
		verify(storage).saveAnswers(model);
		reset(storage);

		model = questionnaire.model;
		Instrumentation ins = getInstrumentation();
		ins.callActivityOnPause(questionnaire);
		solo.clickOnView(solo.getView(R.id.previousWeek));
		verify(storage).saveAnswers(model);
		reset(storage);

		app.getModelManager().reset();
		verify(storage).clear();
	}

	public void testReadFromStorage() throws Exception {
		Questionnaire questionnaire = getActivity();
		InfektionsdagbokApplication app = (InfektionsdagbokApplication) questionnaire.getApplication();
		Storage storage = app.getStorage();
		reset(storage);

		// Back and forward to get back to original week
		solo.clickOnView(solo.getView(R.id.nextWeek));
		solo.clickOnView(solo.getView(R.id.previousWeek));

		verify(storage).getAnswersForWeek(new Week(new DateTime()));
	}

	public void testExceptionInStorageGivesNotification() throws Exception {
		String msg = "PROBLEM";

		Questionnaire questionnaire = getActivity();
		InfektionsdagbokApplication app = (InfektionsdagbokApplication) questionnaire.getApplication();
		ModelManager mgr = app.getModelManager();
		Storage storage = mgr.getStorage();

		doThrow(new Exception(msg)).when(storage).saveAnswers(any(WeekAnswers.class));

		solo.clickOnView(solo.getView(R.id.nextWeek));
		assertTrue("Next week: Should give error message", solo.searchText(msg));

		solo.clickOnView(solo.getView(R.id.previousWeek));
		assertTrue("Prev week: Should give error message", solo.searchText(msg));
	}

	public void testAlarmForNotificationIsSet() throws Exception {
		Questionnaire questionnaire = getActivity();
		InfektionsdagbokApplication app = (InfektionsdagbokApplication) questionnaire.getApplication();

		AlarmManager mgr = app.getAlarmManager();

		DateTime startInstant = new DateTime();
		startInstant = startInstant.dayOfWeek().setCopy(DateTimeConstants.SUNDAY);
		startInstant = startInstant.millisOfDay().setCopy(19 * 60 * 60 * 1000);

		long week = 7 * 24 * 60 * 60 * 1000;

		// TODO: Check relevant arguments so that it has proper intent
		verify(mgr).setRepeating(eq(AlarmManager.RTC_WAKEUP), eq(startInstant.getMillis()), eq(week), any(PendingIntent.class));
	}
}

