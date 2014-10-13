package se.fermitet.android.infektionsdagbok.test;

import static org.mockito.Mockito.mock;
import se.fermitet.android.infektionsdagbok.app.InfektionsdagbokFactory;
import se.fermitet.android.infektionsdagbok.storage.EmailHandler;
import android.content.Context;


public class MockedEmailFactory extends InfektionsdagbokFactory {

	public MockedEmailFactory(Context context) {
		super(context);
	}

	@Override
	public EmailHandler createEmailHandler() {
		return mock(EmailHandler.class);
	}

}
