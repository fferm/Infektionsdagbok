package se.fermitet.android.infektionsdagbok.activity;

import java.util.ArrayList;
import java.util.Collection;

import org.joda.time.LocalDate;

import se.fermitet.android.infektionsdagbok.model.SickDay;
import se.fermitet.android.infektionsdagbok.views.SickDayAdapter;
import android.view.View;

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
	protected void checkDetailEditorsEmpty() {
		fail("unimplemented");
	}

	@Override
	protected SickDay getTestItem() throws Exception {
		fail("unimplemented");
		return null;
	}

	@Override
	protected void editUIBasedOnItem(SickDay itemWithNewValues)	throws Exception {
		fail("unimplemented");
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
		fail("unimplemented");
	}

}
