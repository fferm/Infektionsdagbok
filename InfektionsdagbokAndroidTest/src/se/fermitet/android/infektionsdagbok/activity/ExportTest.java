package se.fermitet.android.infektionsdagbok.activity;


public class ExportTest extends ActivityTestWithSolo<ExportActivity> {

	public ExportTest() {
		super(ExportActivity.class);
	}

	public void testInitials() throws Exception {
	    assertTrue("First Week", solo.waitForText("F�rsta veckan"));
	}

}
