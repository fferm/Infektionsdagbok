package se.fermitet.android.infektionsdagbok.activity;

import org.joda.time.LocalDate;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.SickDay;
import se.fermitet.android.infektionsdagbok.widget.DateTextView;

public class SickDayDetailActivityTest extends DetailActivityTest<SickDay, SickDayDetailActivity>{

	public SickDayDetailActivityTest() {
		super(SickDayDetailActivity.class);
	}

	@Override
	protected String getExpectedHeaderText() {
		return "Sjukskrivning";
	}

	@Override
	protected void checkSubInitials() throws Exception {
		checkHeaderTextView(R.id.startHeader, "Start:");
		assertNotNull("Start date text field", solo.getView(R.id.startTV));
		assertTrue("Start date field class", solo.getView(R.id.startTV) instanceof DateTextView);

		checkHeaderTextView(R.id.endHeader, "Slut:");
		assertNotNull("End date text field", solo.getView(R.id.endTV));
		assertTrue("End date field class", solo.getView(R.id.endTV) instanceof DateTextView);

	}

	@Override
	public void testChangingFieldsChangesModel() throws Exception {
		SickDay model = getActivity().view.getModel();

		// Change start date
		final LocalDate newStart = LocalDate.now().minusMonths(2);
		final DateTextView startDtv = (DateTextView) solo.getView(R.id.startTV);
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					startDtv.setModel(newStart);
				} catch (Exception e) {
					getActivity().view.handleException(e);
				}
			}
		});

		LocalDate dateFromItem = null;
		setStart();
		do {
			dateFromItem = model.getStart();
			setElapsed();
		} while (! newStart.equals(dateFromItem) && notYetTimeout());
		assertEquals("Starting date", newStart, dateFromItem);


		// Change end date
		final LocalDate newEnd = LocalDate.now().minusDays(2);
		final DateTextView endDtv = (DateTextView) solo.getView(R.id.endTV);
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					endDtv.setModel(newEnd);
				} catch (Exception e) {
					getActivity().view.handleException(e);
				}
			}
		});

		dateFromItem = null;
		setStart();
		do {
			dateFromItem = model.getEnd();
			setElapsed();
		} while (! newEnd.equals(dateFromItem) && notYetTimeout());
		assertEquals("End date", newEnd, dateFromItem);
	}

	public void testClickingHeadersOpensDateEditors() throws Exception {
		solo.clickOnView(solo.getView(R.id.startHeader));
		assertTrue("Wait for dialog to open on start", solo.waitForDialogToOpen());

		solo.clickOnText("Ställ in");
		solo.waitForDialogToClose();

		solo.clickOnView(solo.getView(R.id.endHeader));
		assertTrue("Wait for dialog to open on end", solo.waitForDialogToOpen());
	}
}
