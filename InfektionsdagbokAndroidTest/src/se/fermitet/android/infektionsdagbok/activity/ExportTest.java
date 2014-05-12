package se.fermitet.android.infektionsdagbok.activity;


public class ExportTest extends ActivityTestWithSolo<Export> {

	public ExportTest() {
		super(Export.class);
	}
	
	public void testInitials() throws Exception {
	    assertTrue("First Week", solo.waitForText("Starting Week"));
	}

}
