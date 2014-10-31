package se.fermitet.android.infektionsdagbok.activity;

import java.util.Map;
import java.util.UUID;

import org.joda.time.LocalDate;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.ModelManager;
import se.fermitet.android.infektionsdagbok.model.ModelObjectBase;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import se.fermitet.android.infektionsdagbok.storage.Storage;
import se.fermitet.android.infektionsdagbok.views.InfektionsdagbokListAdapter;
import se.fermitet.android.infektionsdagbok.views.InfektionsdagbokView;
import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public abstract class MasterActivityTest_TestData
	<ITEM extends ModelObjectBase,
	VIEW extends View & InfektionsdagbokView,
	ACTIVITY extends InfektionsdagbokActivity<VIEW>,
	ADAPTER extends InfektionsdagbokListAdapter<ITEM>> extends ActivityTestWithSolo<ACTIVITY>{

	protected ModelManager mm;
	private Class<ITEM> itemClass;
	private Class<ACTIVITY> masterActivityClass;
	@SuppressWarnings("rawtypes")
	private Class<? extends InfektionsdagbokActivity> detailActivityClass;

	@SuppressWarnings("rawtypes")
	public MasterActivityTest_TestData(Class<ACTIVITY> masterActivityClass, Class<? extends InfektionsdagbokActivity> detailActivityClass, Class<ITEM> itemClass) {
		super(masterActivityClass);

		this.masterActivityClass = masterActivityClass;
		this.detailActivityClass = detailActivityClass;
		this.itemClass = itemClass;
	}

	@Override
	protected void onBeforeActivityCreation() throws Exception {
		super.onBeforeActivityCreation();

		mm = new ModelManager(new Storage(getInstrumentation().getTargetContext()));

		saveTestData();
	}

	abstract void saveTestData() throws Exception;

	public void testSuperInitials() throws Exception {
		assertNotNull("List view", solo.getView(R.id.itemListView));

		ImageButton editBTN = (ImageButton) solo.getView(R.id.editBTN);
		assertNotNull("Edit button", editBTN);
		assertFalse("Edit button enabled", editBTN.isEnabled());

		ImageButton deleteBTN = (ImageButton) solo.getView(R.id.deleteBTN);
		assertNotNull("Delete button", deleteBTN);
		assertFalse("Delete button enabled", deleteBTN.isEnabled());

		ImageButton newBTN = (ImageButton) solo.getView(R.id.newBTN);
		assertNotNull("New button", newBTN);
		assertTrue("New button enabled", newBTN.isEnabled());
	}

	public void testClickingOnItemHighlightsItAndAffectsButtonsEnabledState() throws Exception {
		ImageButton editBTN = (ImageButton) solo.getView(R.id.editBTN);
		ImageButton deleteBTN = (ImageButton) solo.getView(R.id.deleteBTN);
		ImageButton newBTN = (ImageButton) solo.getView(R.id.newBTN);

		timeoutCheckAdapterSelected(null);
		timeoutCheckButtonEnabled("edit button enabled", editBTN, false);
		timeoutCheckButtonEnabled("deleteButton enabled", deleteBTN, false);
		timeoutCheckButtonEnabled("new button enabled", newBTN, true);

		solo.clickInList(1);
		timeoutCheckAdapterSelected(1);
		timeoutCheckButtonEnabled("edit button enabled", editBTN, true);
		timeoutCheckButtonEnabled("deleteButton enabled", deleteBTN, true);
		timeoutCheckButtonEnabled("new button enabled", newBTN, true);

		solo.clickInList(2);
		timeoutCheckAdapterSelected(2);
		timeoutCheckButtonEnabled("edit button enabled", editBTN, true);
		timeoutCheckButtonEnabled("deleteButton enabled", deleteBTN, true);
		timeoutCheckButtonEnabled("new button enabled", newBTN, true);

		solo.clickInList(2);
		timeoutCheckAdapterSelected(null);
		timeoutCheckButtonEnabled("edit button enabled", editBTN, false);
		timeoutCheckButtonEnabled("deleteButton enabled", deleteBTN, false);
		timeoutCheckButtonEnabled("new button enabled", newBTN, true);
	}

	protected void timeoutCheckAdapterSelected(Integer positionOrNull) throws Exception {
		ADAPTER adapter = getListAdapter();

		Integer selectedPosition = null;
		boolean condition;
		setStart();
		do {
			selectedPosition = adapter.getSelectedPosition();

			if (positionOrNull == null) {
				condition = selectedPosition == null;
			} else {
				if (selectedPosition != null) condition = ((positionOrNull - 1) == (selectedPosition));
				else condition = false;
			}
			setElapsed();
		} while (!condition && notYetTimeout());

		if (positionOrNull == null) {
			assertNull("Should have no selection", selectedPosition);
		} else {
			assertEquals("Selection position",  positionOrNull - 1, (int) selectedPosition);
		}
	}

	private void timeoutCheckButtonEnabled(String message, ImageButton button, boolean shouldBeEnabled) throws Exception {
		setStart();
		do {
			setElapsed();
		} while (button.isEnabled() != shouldBeEnabled && notYetTimeout());
		assertTrue(message, button.isEnabled() == shouldBeEnabled);
	}

	public void testDelete_fromMaster() throws Exception {
		solo.clickInList(1);
		timeoutCheckAdapterSelected(1);

		ADAPTER adapter = getListAdapter();

		ITEM toDelete = adapter.getItem(0);

		solo.clickOnView(solo.getView(R.id.deleteBTN));

		checkItemIsDeleted(toDelete);
	}

	public void testDelete_fromDetail() throws Exception {
		solo.clickInList(1);
		timeoutCheckAdapterSelected(1);

		ADAPTER adapter = getListAdapter();
		ITEM toDelete = adapter.getItem(0);

		solo.clickOnView(solo.getView(R.id.editBTN));
		timeoutGetCurrentActivity(masterActivityClass);

		// Check delete button enabled
		ImageButton deleteBTN = (ImageButton) solo.getView(R.id.deleteBTN);
		assertTrue("delete button enabled", deleteBTN.isEnabled());

		solo.clickOnView(solo.getView(R.id.deleteBTN));
		timeoutGetCurrentActivity(detailActivityClass);

		checkItemIsDeleted(toDelete);
	}

	public void testClickingOnNewOpensEmptyDetailActivityPlusBackNavigation() throws Exception {
		solo.clickOnView(solo.getView(R.id.newBTN));

		// Check that new activity is started
		timeoutGetCurrentActivity(detailActivityClass);

		checkDetailEditorsEmpty();

		// Check delete button disabled
		ImageButton deleteBTN = (ImageButton) solo.getView(R.id.deleteBTN);
		assertFalse("delete button enabled", deleteBTN.isEnabled());

		// Check back navigation
		solo.clickOnActionBarHomeButton();
		timeoutGetCurrentActivity(masterActivityClass);
	}

	protected abstract void checkDetailEditorsEmpty();

	private void checkItemIsDeleted(ITEM toDelete) throws Exception {
		// Check file
		boolean contains;
		setStart();
		do {
			@SuppressWarnings("unchecked")
			Map<UUID, ITEM> fromFile = (Map<UUID, ITEM>) mm.getAllOfCorrectClass(itemClass);

			contains = fromFile.containsKey(toDelete.getUUID());

			setElapsed();
		} while(contains && notYetTimeout());
		assertFalse("Saved data contains deleted item", contains);

		// Check adapter
		setStart();
		do {
			setElapsed();
		} while(adapterContains(toDelete) && notYetTimeout());
		assertFalse("Adapter contains deleted item", adapterContains(toDelete));
	}

	public void testEditAndSave() throws Exception {
		int sizeBefore = mm.getAllTreatments().size();

		solo.clickInList(1);
		timeoutCheckAdapterSelected(1);

		ADAPTER adapter = getListAdapter();
		ITEM toEdit = adapter.getItem(0);
		UUID uuid = toEdit.getUUID();
		ITEM withNewValues = getEditTestItem();
		withNewValues.setUUID(uuid);

		solo.clickOnView(solo.getView(R.id.editBTN));
		TreatmentDetailActivity detailActivity = (TreatmentDetailActivity) timeoutGetCurrentActivity(TreatmentDetailActivity.class);

		// Start editing
		TextView startTV = (TextView) solo.getView(R.id.startTV);
		solo.clickOnView(startTV);
		solo.waitForDialogToOpen();
		DatePickerDialog dialog = detailActivity.view.getDatePickerDialog();
		DatePicker picker = dialog.getDatePicker();

		LocalDate start = withNewValues.getStartingDate();
		solo.setDatePicker(picker, start.getYear(), start.getMonthOfYear() - 1, start.getDayOfMonth());
		solo.clickOnButton("Ställ in");

		EditText numDaysEdit = (EditText) solo.getView(R.id.numDaysEdit);
		solo.clearEditText(numDaysEdit);
		solo.enterText(numDaysEdit, withNewValues.getNumDays().toString());

		EditText medicineEdit = (EditText) solo.getView(R.id.medicineEdit);
		solo.clearEditText(medicineEdit);
		solo.enterText(medicineEdit, withNewValues.getMedicine());

		EditText infectionTypeEdit = (EditText) solo.getView(R.id.infectionTypeEdit);
		solo.clearEditText(infectionTypeEdit);
		solo.enterText(infectionTypeEdit, withNewValues.getInfectionType());

		solo.clickOnView(solo.getView(R.id.saveBTN));
		timeoutGetCurrentActivity(TreatmentMasterActivity.class);


		// Check file
		Map<UUID, Treatment> treatmentsFromFile = null;
		boolean condition;
		setStart();
		do {
			treatmentsFromFile = mm.getAllTreatments();
			Treatment fromFile = treatmentsFromFile.get(uuid);

			condition = withNewValues.equals(fromFile);

			setElapsed();
		} while(!condition && notYetTimeout());
		assertTrue("Found in file", condition);
		assertEquals("Number of items on file", sizeBefore, treatmentsFromFile.size());

		// Check adapter
		setStart();
		do {
			adapter = getListAdapter();
			condition = adapterContains(withNewValues);

			setElapsed();
		} while(!condition && notYetTimeout());
		assertTrue("Found in adapter", condition);
		assertEquals("Number of items in adapter", sizeBefore, adapter.getCount());
	}

	protected abstract ITEM getEditTestItem();




	@SuppressWarnings("unchecked")
	protected ADAPTER getListAdapter() {
		ACTIVITY activity = getActivity();
		ListView listView = (ListView) activity.view.findViewById(R.id.itemListView);
		return (ADAPTER) listView.getAdapter();
	}

	protected boolean adapterContains(ITEM item) {
		ListAdapter adapter = getListAdapter();
		for(int i = 0; i < adapter.getCount(); i++) {
			@SuppressWarnings("unchecked")
			ITEM inAdapter = (ITEM) adapter.getItem(i);

			if(inAdapter.equals(item)) return true;
		}
		return false;
	}

	protected int indexOfItemInAdapter(ITEM item) {
		ListAdapter adapter = getListAdapter();
		for(int i = 0; i < adapter.getCount(); i++) {
			@SuppressWarnings("unchecked")
			ITEM inAdapter = (ITEM) adapter.getItem(i);

			if(inAdapter.equals(item)) return i;
		}
		return -1;
	}





}
