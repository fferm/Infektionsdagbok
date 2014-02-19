package se.fermitet.android.infektionsdagbok;

import static org.mockito.Mockito.*;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.app.Factory;
import se.fermitet.android.infektionsdagbok.app.InfektionsdagbokApplication;
import se.fermitet.android.infektionsdagbok.helper.NameFromIdHelper;
import se.fermitet.android.infektionsdagbok.model.Week;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;
import se.fermitet.android.infektionsdagbok.storage.Storage;
import se.fermitet.android.infektionsdagbok.views.QuestionView;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
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

	public void testStatusOfGenerallyWellChangesEnabledStateOfOthers() throws Exception {
		clickOnQuestionWithId(R.id.generallyWell);

		for (Integer idObj : WeekAnswers.questionIds) {
			int id = idObj.intValue();
			assertEnabled(id, true);
		}
		assertChecked(R.id.generallyWell, false);

		clickOnQuestionWithId(R.id.dryCough); // Change some state
		clickOnQuestionWithId(R.id.generallyWell);

		for (Integer idObj : WeekAnswers.questionIds) {
			int id = idObj.intValue();

			assertEnabled(id,  id == R.id.generallyWell);
		}

		assertChecked(R.id.dryCough, true);
		assertChecked(R.id.generallyWell, true);
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
		int questionId = R.id.generallyWell;
		QuestionView questionView = (QuestionView) solo.getView(questionId);
		View selector = questionView.findViewById(R.id.answerSelector);
		View text = questionView.findViewById(R.id.questionText);

		assertClickingQuestionPart(questionId, selector, "selector", true);
		assertClickingQuestionPart(questionId, text, "text view", true);
		assertClickingQuestionPart(questionId, questionView, "full question", true);

	}

	public void testClickingDisabledQuestionsDoesNotChangeAnswer() throws Exception {
		int questionId = R.id.malaise;
		QuestionView questionView = (QuestionView) solo.getView(questionId);
		View selector = questionView.findViewById(R.id.answerSelector);
		View text = questionView.findViewById(R.id.questionText);

		assertFalse("Should be disabled for this test", questionView.isEnabled());

		assertClickingQuestionPart(questionId, selector, "selector", false);
		assertClickingQuestionPart(questionId, text, "text view", false);
		assertClickingQuestionPart(questionId, questionView, "full question", false);
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

	// TODO, fix this with some injection
	/*	public void testExceptionInStorageGivesNotification() throws Exception {
		String nextMsg = "PROBLEM: nextWeek";
		String prevMsg = "PROBLEM: prevWeek";

		Questionnaire questionnaire = getActivity();
		ModelManager mgr = mock(ModelManager.class);
		when(mgr.getNextWeekAnswers(any(WeekAnswers.class))).thenThrow(new Exception(nextMsg));
		when(mgr.getPreviousWeekAnswers(any(WeekAnswers.class))).thenThrow(new Exception(prevMsg));

		questionnaire.setModelManager(mgr);

		solo.clickOnView(solo.getView(R.id.nextWeek));
		assertTrue("Next week: Should give error message", solo.searchText(nextMsg));

		solo.clickOnView(solo.getView(R.id.previousWeek));
		assertTrue("Prev week: Should give error message", solo.searchText(prevMsg));
	}*/

	public static class MockedStorageFactory implements Factory, Parcelable {
		@Override
		public Storage createStorage() {
			return mock(Storage.class);
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {}

		public static final Parcelable.Creator<MockedStorageFactory> CREATOR = new Parcelable.Creator<MockedStorageFactory>() {
			@Override
			public MockedStorageFactory createFromParcel(Parcel source) {
				return new MockedStorageFactory();
			}

			@Override
			public MockedStorageFactory[] newArray(int size) {
				return new MockedStorageFactory[size];
			}
		};
	}



	private void assertClickingQuestionPart(int questionId, View viewToClick, String nameOfView, boolean shouldChange) {
		boolean before = getActivity().model.getAnswer(questionId);
		solo.clickOnView(viewToClick);
		boolean after = getActivity().model.getAnswer(questionId);


		if (shouldChange) {
			assertFalse("Should have changed after clicking " + nameOfView, before == after);
		} else {
			assertTrue("Should not have changed after clicking the disabled version of " + nameOfView, before == after);
		}
	}




}

