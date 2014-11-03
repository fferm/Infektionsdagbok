package se.fermitet.android.infektionsdagbok.activity;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.ModelManager;
import se.fermitet.android.infektionsdagbok.model.ModelObjectBase;
import se.fermitet.android.infektionsdagbok.storage.Storage;
import se.fermitet.android.infektionsdagbok.views.InfektionsdagbokListAdapter;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

@SuppressWarnings("rawtypes")
public abstract class MasterActivityTest
	<ITEM extends ModelObjectBase,
	ACTIVITY extends InfektionsdagbokActivity,
	ADAPTER extends InfektionsdagbokListAdapter<ITEM>> extends ActivityTestWithSolo<ACTIVITY>{

	protected ModelManager mm;
	protected Class<ACTIVITY> masterActivityClass;
	protected Class<? extends InfektionsdagbokActivity> detailActivityClass;
	protected Class<ITEM> itemClass;

	protected abstract void saveTestData() throws Exception;
	protected abstract String getExpectedHeaderText() throws Exception;
	protected abstract void checkSubInitials() throws Exception;
	protected abstract Collection<ITEM> getSpecialItemsToCheck();
	protected abstract void checkListOrder(ITEM previous, ITEM current);
	protected abstract void checkListSubViewForItemData(View listSubView, ITEM item) throws Exception;
	protected abstract void checkDetailEditorsEmpty() throws Exception;
	protected abstract void checkDetailEditorsContents(ITEM item) throws Exception;
	protected abstract ITEM getTestItem() throws Exception;
	protected abstract void editUIBasedOnItem(ITEM itemWithNewValues) throws Exception;

	public MasterActivityTest(Class<ACTIVITY> masterActivityClass, Class<? extends InfektionsdagbokActivity> detailActivityClass, Class<ITEM> itemClass) {
		super(masterActivityClass);

		this.masterActivityClass = masterActivityClass;
		this.detailActivityClass = detailActivityClass;
		this.itemClass = itemClass;
	}

	@Override
	protected void onBeforeActivityCreation() throws Exception {
		super.onBeforeActivityCreation();

		mm = new ModelManager(new Storage(getInstrumentation().getTargetContext()));

		if (!this.getName().equals("testEnterNewTreatment")) saveTestData();
	}

	public void testInitials() throws Exception {
		TextView tv = (TextView) solo.getView(R.id.title);
		assertEquals("Header text", getExpectedHeaderText(), tv.getText());

		ImageButton editBTN = (ImageButton) solo.getView(R.id.editBTN);
		assertNotNull("Edit button", editBTN);
		assertFalse("Edit button enabled", editBTN.isEnabled());

		ImageButton deleteBTN = (ImageButton) solo.getView(R.id.deleteBTN);
		assertNotNull("Delete button", deleteBTN);
		assertFalse("Delete button enabled", deleteBTN.isEnabled());

		ImageButton newBTN = (ImageButton) solo.getView(R.id.newBTN);
		assertNotNull("New button", newBTN);
		assertTrue("New button enabled", newBTN.isEnabled());

		assertNotNull("List view", solo.getView(R.id.itemListView));

		@SuppressWarnings("unchecked")
		Collection<ITEM> testData = (Collection<ITEM>) mm.getAllItemsOfClass(itemClass).values();

		for (ITEM item : testData) {
			searchForItemInListAndCheckDisplayedValues(item);
		}

		for (ITEM item : getSpecialItemsToCheck()) {
			searchForItemInListAndCheckDisplayedValues(item);
		}

		checkSubInitials();
	}

	public void testOrderInList() throws Exception {
		ADAPTER adapter = getListAdapter();

		ITEM previous = null;
		ITEM current = null;

		for (int i = 0; i < adapter.getCount(); i++) {
			current = adapter.getItem(i);

			if (i > 1) {
				checkListOrder(previous, current);
			}

			previous = current;
		}
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

	@SuppressWarnings("unchecked")
	public void testClickingOnListViewFillsEditors() throws Exception {
		ListAdapter adapter = getListAdapter();

		// Regular
		clickOnItemInListClickEditAndCheckEditorContentsThenGoBack((ITEM) adapter.getItem(0));

		// Nulls
		for (ITEM item : getSpecialItemsToCheck()) {
			clickOnItemInListClickEditAndCheckEditorContentsThenGoBack(item);
		}
	}

	private void clickOnItemInListClickEditAndCheckEditorContentsThenGoBack(ITEM item) throws Exception {
		assertTrue("Adapter must contain item: " + item, adapterContains(item));
		int i = indexOfItemInAdapter(item);

		solo.clickInList(i + 1);
		Thread.sleep(100);
		solo.clickOnView(solo.getView(R.id.editBTN));
		timeoutGetCurrentActivity(detailActivityClass);
		checkDetailEditorsContents(item);

		// Cancel to go back
		solo.clickOnView(solo.getView(R.id.cancelBTN));
		timeoutGetCurrentActivity(masterActivityClass);
	}

	public void testDelete_fromMaster() throws Exception {
		solo.clickInList(1);
		timeoutCheckAdapterSelected(1);

		ADAPTER adapter = getListAdapter();

		ITEM toDelete = adapter.getItem(0);

		solo.clickOnView(solo.getView(R.id.deleteBTN));

		checkItemIsDeleted(toDelete);

		checkNothingSelectedAndCorrectButtonsEnabled();
	}

	public void testDelete_fromDetail() throws Exception {
		solo.clickInList(1);
		timeoutCheckAdapterSelected(1);

		ADAPTER adapter = getListAdapter();
		ITEM toDelete = adapter.getItem(0);

		solo.clickOnView(solo.getView(R.id.editBTN));
		timeoutGetCurrentActivity(detailActivityClass);

		// Check delete button enabled
		ImageButton deleteBTN = (ImageButton) solo.getView(R.id.deleteBTN);
		assertTrue("delete button enabled", deleteBTN.isEnabled());

		solo.clickOnView(solo.getView(R.id.deleteBTN));
		timeoutGetCurrentActivity(masterActivityClass);

		checkItemIsDeleted(toDelete);

		checkNothingSelectedAndCorrectButtonsEnabled();
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

		checkNothingSelectedAndCorrectButtonsEnabled();
	}

	private void checkItemIsDeleted(ITEM toDelete) throws Exception {
		// Check file
		boolean contains;
		setStart();
		do {
			@SuppressWarnings("unchecked")
			Map<UUID, ITEM> fromFile = (Map<UUID, ITEM>) mm.getAllItemsOfClass(itemClass);

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

	@SuppressWarnings("unchecked")
	public void testEditAndSave() throws Exception {
		int sizeBefore = mm.getAllItemsOfClass(itemClass).size();

		solo.clickInList(1);
		timeoutCheckAdapterSelected(1);

		ADAPTER adapter = getListAdapter();
		ITEM toEdit = adapter.getItem(0);
		UUID uuid = toEdit.getUUID();
		ITEM withNewValues = getTestItem();
		withNewValues.setUUID(uuid);

		solo.clickOnView(solo.getView(R.id.editBTN));
		timeoutGetCurrentActivity(detailActivityClass);

		editUIBasedOnItem(withNewValues);

		solo.clickOnView(solo.getView(R.id.saveBTN));
		timeoutGetCurrentActivity(masterActivityClass);

		// Check file
		Map<UUID, ITEM> allFromFile = null;
		boolean condition;
		setStart();
		do {
			allFromFile = (Map<UUID, ITEM>) mm.getAllItemsOfClass(itemClass);
			ITEM fromFile = allFromFile.get(uuid);

			condition = withNewValues.equals(fromFile);

			setElapsed();
		} while(!condition && notYetTimeout());
		assertTrue("Found in file", condition);
		assertEquals("Number of items on file", sizeBefore, allFromFile.size());

		// Check adapter
		setStart();
		do {
			adapter = getListAdapter();
			condition = adapterContains(withNewValues);

			setElapsed();
		} while(!condition && notYetTimeout());
		assertTrue("Found in adapter", condition);
		assertEquals("Number of items in adapter", sizeBefore, adapter.getCount());

		checkNothingSelectedAndCorrectButtonsEnabled();
	}

	@SuppressWarnings("unchecked")
	public void testNewWithCancel() throws Exception {
		int sizeBefore = getActivity().getLocalApplication().getModelManager().getAllItemsOfClass(itemClass).size();

		ITEM testItem = getTestItem();

		// Click new
		solo.clickOnView(solo.getView(R.id.newBTN));
		timeoutGetCurrentActivity(detailActivityClass);

		editUIBasedOnItem(testItem);

		// Click save
		solo.clickOnView(solo.getView(R.id.cancelBTN));
		timeoutGetCurrentActivity(masterActivityClass);


		// Check saved data
		Map<UUID, ITEM> allFromFile = null;
		setStart();
		do {
			allFromFile = (Map<UUID, ITEM>) getActivity().getLocalApplication().getModelManager().getAllItemsOfClass(itemClass);

			setElapsed();
		} while (allFromFile.size() != sizeBefore  && notYetTimeout());
		assertEquals("Size after save", sizeBefore, allFromFile.size());

		checkNothingSelectedAndCorrectButtonsEnabled();
	}


	@SuppressWarnings("unchecked")
	public void testEnterNewTreatment() throws Exception {
		int sizeBefore = getActivity().getLocalApplication().getModelManager().getAllTreatments().size();

		ITEM testItem = getTestItem();

		// Click new
		solo.clickOnView(solo.getView(R.id.newBTN));
		timeoutGetCurrentActivity(detailActivityClass);

		editUIBasedOnItem(testItem);

		// Click save
		solo.clickOnView(solo.getView(R.id.saveBTN));
		timeoutGetCurrentActivity(masterActivityClass);

		// Check saved data
		Map<UUID, ITEM> allFromFile = null;
		setStart();
		do {
			allFromFile = (Map<UUID, ITEM>) getActivity().getLocalApplication().getModelManager().getAllItemsOfClass(itemClass);

			setElapsed();
		} while (allFromFile.size() != sizeBefore + 1 && notYetTimeout());
		assertEquals("Size after save", sizeBefore + 1, allFromFile.size());
		ITEM fromFile = allFromFile.values().iterator().next();
		testItem.setUUID(fromFile.getUUID());
		assertEquals("Equals with saved data (Except UUID)", testItem, fromFile);

		// Check in adapter
		ADAPTER adapter = getListAdapter();

		int count;
		setStart();
		do {
			count = adapter.getCount();

			setElapsed();
		} while (count != 1 && notYetTimeout());
		assertEquals("adapter size",  sizeBefore + 1, count);
		assertEquals("Equals with adapter object", testItem, adapter.getItem(0));

		checkNothingSelectedAndCorrectButtonsEnabled();
	}

	private void searchForItemInListAndCheckDisplayedValues(ITEM item) throws Exception {
		ListAdapter adapter = getListAdapter();

		int index = indexOfItemInAdapter(item);
		assertFalse("Didn't find item: " + item, index == -1);

		View listSubView = adapter.getView(index, null, null);

		checkListSubViewForItemData(listSubView, item);
	}

	private void checkNothingSelectedAndCorrectButtonsEnabled() {
		ADAPTER adapter = getListAdapter();
		assertNull("Selection in list", adapter.getSelectedItem());

		ImageButton editBTN = (ImageButton) solo.getView(R.id.editBTN);
		setStart();
		do {
			setElapsed();
		} while (editBTN.isEnabled() && notYetTimeout());
		assertFalse("edit button should be disabled", editBTN.isEnabled());

		ImageButton deleteBTN = (ImageButton) solo.getView(R.id.deleteBTN);
		setStart();
		do {
			setElapsed();
		} while (deleteBTN.isEnabled() && notYetTimeout());
		assertFalse("delete button should be disabled", deleteBTN.isEnabled());
	}


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
