package se.fermitet.android.infektionsdagbok.storage;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.test.AndroidTestCase;

public class EmailHandlerTest extends AndroidTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testMockedCallsOnIntentToSendMail() throws Exception {
		String desiredSubject = "Infektionsdagbok";
		String desiredMessage = "HŠr kommer min senaste infektionsdagbok";

		EmailHandlerMockedIntent emailHandler = new EmailHandlerMockedIntent();

    	File file = new File(this.getContext().getExternalFilesDir(null), "Testfile.xls");

    	Intent mockedIntent = emailHandler.prepareIntent(file);

    	verify(mockedIntent).setType("application/vnd.ms-excel");
    	verify(mockedIntent, never()).putExtra(eq(Intent.EXTRA_EMAIL), (String[]) any());
    	verify(mockedIntent).putExtra(Intent.EXTRA_SUBJECT, desiredSubject);
    	verify(mockedIntent).putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
    	verify(mockedIntent).putExtra(android.content.Intent.EXTRA_TEXT, desiredMessage);
	}
}

class EmailHandlerMockedIntent extends EmailHandler {

	public EmailHandlerMockedIntent() {
		super();
	}

	@Override
	protected Intent createIntent(String action) {
		return mock(Intent.class);
	}

}
