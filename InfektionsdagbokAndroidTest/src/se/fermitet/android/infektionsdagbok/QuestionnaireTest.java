package se.fermitet.android.infektionsdagbok;

import static org.mockito.Mockito.*;

import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.app.Factory;
import se.fermitet.android.infektionsdagbok.app.InfektionsdagbokApplication;
import se.fermitet.android.infektionsdagbok.helper.NameFromIdHelper;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;
import se.fermitet.android.infektionsdagbok.storage.Storage;
import se.fermitet.android.infektionsdagbok.views.QuestionView;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.robotium.solo.Solo;

public class QuestionnaireTest extends ActivityInstrumentationTestCase2<Questionnaire> {

	private Solo solo;

	public QuestionnaireTest() {
		super(Questionnaire.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		super.setActivityIntent(createIntent());
		solo = new Solo(getInstrumentation(), getActivity());
	}

	private Intent createIntent() {
		Intent i = new Intent();
		Bundle b = new Bundle();
		b.putParcelable(Questionnaire.FACTORY_KEY, new MockedStorageFactory());

		i.putExtras(b);
		return i;
	}

	@Override
	protected void tearDown() throws Exception {
		InfektionsdagbokApplication app = (InfektionsdagbokApplication) getActivity().getApplication();

		app.getModelManager().reset();
		solo.finishOpenedActivities();
		super.tearDown();
	}

	public void testInitials() throws Exception {
		assertInitialsOnQuestions();

		TextView weekView = (TextView) solo.getView(R.id.weekDisplay);
		DateTime now = new DateTime();
		assertEquals("week text", "" + now.getYear() + "-" + now.getWeekOfWeekyear(), weekView.getText());
	}

	private void assertInitialsOnQuestions() {
		assertQuestionFullState(R.id.generallyWell, "Väsentligen frisk", true, true);
		assertQuestionFullState(R.id.malaise, "Sjukdomskänsla", false, false);
		assertQuestionFullState(R.id.fever, "Feber > 38", false, false);
		assertQuestionFullState(R.id.earAche, "Öronvärk", false, false);
		assertQuestionFullState(R.id.soreThroat, "Halsont", false, false);
		assertQuestionFullState(R.id.runnyNose, "Snuva", false, false);
		assertQuestionFullState(R.id.stommacAche, "Magbesvär", false, false);
		assertQuestionFullState(R.id.dryCough, "Torrhosta", false, false);
		assertQuestionFullState(R.id.wetCough, "Slemhosta", false, false);
		assertQuestionFullState(R.id.morningCough, "Morgonupphostning", false, false);
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
			assertClickingOnOneAnswer(model,  id);
		}
	}


	private void assertClickingOnOneAnswer(WeekAnswers wa, int id) {
		boolean before = wa.getAnswer(id);

		clickOnQuestionWithId(id);
		assertTrue(NameFromIdHelper.getNameFromId(id) + " after", before != wa.getAnswer(id));
	}

	public void testClickingArrowNavigation() throws Exception {
		clickOnQuestionWithId(R.id.generallyWell); // Change someting so that later check does not compare with default

		WeekAnswers beforeModel = getActivity().model;

		assertFalse("beforeModel generallyWell should have changed", beforeModel.getAnswer(R.id.generallyWell));

		solo.clickOnView(solo.getView(R.id.previousWeek));

		WeekAnswers afterModel = getActivity().model;

		assertFalse("not same model", beforeModel == afterModel);
		assertFalse("not equal models", beforeModel.equals(afterModel));
		assertTrue("week before model", beforeModel.week.previous().equals(afterModel.week));

		solo.clickOnView(solo.getView(R.id.nextWeek));

		WeekAnswers nextModel = getActivity().model;

		assertEquals("same week again after going back and forward", beforeModel.week, nextModel.week);
		assertEquals("week answers equality when going back and forward", beforeModel, nextModel);
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
		public void writeToParcel(Parcel dest, int flags) {
		}

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

	public void testFactoryInjection() throws Exception {
		InfektionsdagbokApplication app = (InfektionsdagbokApplication) getActivity().getApplication();
		Factory factory = app.getFactory();
		assertEquals("factory class", MockedStorageFactory.class, factory.getClass());
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

	// TODO: Fix with injection
	/*
	public void testReadFromStorage() throws Exception {
		Questionnaire questionnaire = getActivity();
		InfektionsdagbokApplication app = (InfektionsdagbokApplication) questionnaire.getApplication();

		Storage storage = mock(Storage.class);
		app.getModelManager().setStorage(storage);

		// Back and forward to get back to original week
		solo.clickOnView(solo.getView(R.id.nextWeek));
		solo.clickOnView(solo.getView(R.id.previousWeek));

		verify(storage).getAnswersForWeek(new Week(new DateTime()));
	}*/

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


	private void assertQuestionFullState(int id, String questionText, boolean checked, boolean enabled) {
		QuestionView view = (QuestionView) solo.getView(id);
		assertNotNull(view);

		assertText(id, questionText);
		assertChecked(id, checked);
		assertEnabled(id, enabled);
	}

	private void assertText(int id, String text) {
		QuestionView view = (QuestionView) solo.getView(id);

		TextView tv = (TextView) view.findViewById(R.id.questionText);
		assertEquals(NameFromIdHelper.getNameFromId(id) + " question text", text, tv.getText());
	}

	private void assertChecked(int id, boolean shouldBeChecked) {
		QuestionView view = (QuestionView) solo.getView(id);

		CompoundButton selector =  (CompoundButton) view.findViewById(R.id.answerSelector);
		assertEquals(NameFromIdHelper.getNameFromId(id) + " selector has wrong state", shouldBeChecked, selector.isChecked());

		if (shouldBeChecked) {
			Drawable background = view.getBackground();
			assertTrue(NameFromIdHelper.getNameFromId(id) + " should have a ColorDrawable background", background instanceof ColorDrawable);
			ColorDrawable colorBackground = (ColorDrawable) background;
			assertTrue(NameFromIdHelper.getNameFromId(id) +  "should have a non alpha-null background", colorBackground.getAlpha() > 0);
		} else {
			assertNull(NameFromIdHelper.getNameFromId(id) + " should not have a background", view.getBackground());
		}
	}

	private void assertEnabled(int id, boolean enabled) {
		QuestionView view = (QuestionView) solo.getView(id);

		CompoundButton selector =  (CompoundButton) view.findViewById(R.id.answerSelector);
		TextView tv = (TextView) view.findViewById(R.id.questionText);

		assertTrue(NameFromIdHelper.getNameFromId(id) + " text should be enabled", tv.isEnabled());
		assertEquals(NameFromIdHelper.getNameFromId(id) + " selector enabled value", enabled, selector.isEnabled());
	}

	private void clickOnQuestionWithId(int id) {
		QuestionView questionView = (QuestionView) solo.getView(id);
		solo.clickOnView(questionView);
	}
}

