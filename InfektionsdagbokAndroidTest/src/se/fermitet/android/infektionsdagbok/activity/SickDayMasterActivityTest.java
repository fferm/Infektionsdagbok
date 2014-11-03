package se.fermitet.android.infektionsdagbok.activity;

import java.util.ArrayList;
import java.util.Collection;

import org.joda.time.LocalDate;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.SickDay;
import se.fermitet.android.infektionsdagbok.views.SickDayAdapter;
import se.fermitet.android.infektionsdagbok.widget.DateTextView;
import android.view.View;
import android.widget.TextView;

public class SickDayMasterActivityTest extends MasterActivityTest<SickDay, SickDayMasterActivity, SickDayAdapter> {

	private SickDay nullStart;
	private SickDay nullEnd;

	public SickDayMasterActivityTest() {
		super(SickDayMasterActivity.class, SickDayDetailActivity.class, SickDay.class);
	}

	@Override
	protected void saveTestData() throws Exception {
		ArrayList<SickDay> testData = new ArrayList<SickDay>();

		for (int i = 1; i <= 5; i++) {
			LocalDate start = LocalDate.now().minusMonths(i);
			LocalDate end = LocalDate.now().minusWeeks(i);

			testData.add(new SickDay(start, end));
		}

		nullStart = new SickDay(null, LocalDate.now().minusDays(10));
		nullEnd = new SickDay(LocalDate.now().minusDays(100), null);

		testData.add(nullStart);
		testData.add(nullEnd);

		mm.saveAll(testData);
	}

	@Override
	protected String getExpectedHeaderText() throws Exception {
		return "Sjukskrivningar";
	}

	@Override
	protected void checkSubInitials() throws Exception {
		assertTrue("Start list header", solo.waitForText("Start"));
		assertTrue("End list header", solo.waitForText("Slut"));
	}

	@Override
	protected void checkListOrder(SickDay previous, SickDay current) {
		LocalDate previousStart = previous.getStart();
		LocalDate currentStart = current.getStart();

		boolean condition = (previousStart == null) || (currentStart == null) || previousStart.isAfter(currentStart);
		assertTrue("Wrong order on items starting with the one with start =  " + current.getStart(), condition);
	}

	@Override
	protected void checkDetailEditorsEmpty() {
		TextView startTV = (TextView) solo.getView(R.id.startTV);
		CharSequence startText = startTV.getText();
		assertTrue("Start date empty", startText == null || startText.length() == 0);

		TextView endTV = (TextView) solo.getView(R.id.endTV);
		CharSequence endText = endTV.getText();
		assertTrue("End date empty", endText == null || endText.length() == 0);
	}

	@Override
	protected void checkDetailEditorsContents(SickDay item) {
		TextView startTv = (TextView) solo.getView(R.id.startTV);
		checkTextViewForStart(item, startTv);

		TextView endTv = (TextView) solo.getView(R.id.endTV);
		checkTextViewForEnd(item, endTv);
	}

	private void checkTextViewForStart(SickDay item, TextView startTv) {
		checkDateTextView(item.getStart(), startTv);
	}

	private void checkTextViewForEnd(SickDay item, TextView endTv) {
		checkDateTextView(item.getEnd(), endTv);
	}

	@Override
	protected SickDay getTestItem() throws Exception {
		LocalDate start = new LocalDate(1990, 1, 1);
		LocalDate end = new LocalDate(1991, 1, 1);
		return new SickDay(start, end);
	}

	@Override
	protected void editUIBasedOnItem(SickDay itemWithNewValues)	throws Exception {
		DateTextView startTV = (DateTextView) solo.getView(R.id.startTV);
		startTV.setModel(itemWithNewValues.getStart());

		DateTextView endTV = (DateTextView) solo.getView(R.id.endTV);
		endTV.setModel(itemWithNewValues.getEnd());
	}

	@Override
	protected Collection<SickDay> getSpecialItemsToCheck() {
		Collection<SickDay> ret = new ArrayList<SickDay>();

		ret.add(nullEnd);
		ret.add(nullStart);

		return ret;
	}

	@Override
	protected void checkListSubViewForItemData(View listSubView, SickDay item) {
		TextView startTv = (TextView) listSubView.findViewById(R.id.startValueField);
		TextView endTv = (TextView) listSubView.findViewById(R.id.endValueField);

		LocalDate start = item.getStart();
		if (start == null) {
			assertTrue("Should show null date", (startTv.getText() == null) || (startTv.getText().length() == 0));
		} else {
			assertEquals("Should show start date ", item.getStartString(), startTv.getText());
		}

		LocalDate end = item.getEnd();
		if (end == null) {
			assertTrue("Should show null date", (endTv.getText() == null) || (endTv.getText().length() == 0));
		} else {
			assertEquals("Should show end date ", item.getEndString(), endTv.getText());
		}
	}


}
