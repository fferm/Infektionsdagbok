package se.fermitet.android.infektionsdagbok.activity;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.model.ModelObjectBase;
import android.widget.ImageButton;
import android.widget.TextView;

@SuppressWarnings("rawtypes")
public abstract class DetailActivityTest<ITEM extends ModelObjectBase, ACTIVITY extends InfektionsdagbokActivity> extends ActivityTestWithSolo<ACTIVITY> {

	protected abstract String getExpectedHeaderText();
	protected abstract void checkSubInitials();

	public DetailActivityTest(Class<ACTIVITY> detailActivityClass) {
		super(detailActivityClass);
	}

	public void testInitials() throws Exception {
		TextView tv = (TextView) solo.getView(R.id.title);
		assertEquals("Header text", getExpectedHeaderText(), tv.getText());

		ImageButton saveBTN = (ImageButton) solo.getView(R.id.saveBTN);
		assertNotNull("Save button", saveBTN);
		assertTrue("save button enabled", saveBTN.isEnabled());

		ImageButton cancelBTN = (ImageButton) solo.getView(R.id.cancelBTN);
		assertNotNull("Cancel button", cancelBTN);
		assertTrue("cancel button enabled", cancelBTN.isEnabled());

		ImageButton deleteBTN = (ImageButton) solo.getView(R.id.deleteBTN);
		assertNotNull("Delete button", deleteBTN);
		assertFalse("Delete button enabled", deleteBTN.isEnabled());

		checkSubInitials();
	}





}
