package se.fermitet.android.infektionsdagbok.test;

import static org.mockito.Mockito.mock;
import android.content.Context;
import android.os.Vibrator;

public class MockedVibratorFactory extends DoNotHandleExceptionsFactory {

	public MockedVibratorFactory(Context context) {
		super(context);
	}

	@Override
	public Vibrator getVibrator() {
		return mock(Vibrator.class);
	}

}
