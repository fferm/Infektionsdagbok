package se.fermitet.android.infektionsdagbok.app;

import android.app.Instrumentation;
import android.test.InstrumentationTestCase;

public class AlarmTest extends InstrumentationTestCase {
	public void testALaunch() throws Exception {
		Instrumentation instrumentation = this.getInstrumentation();
		System.out.println("!!!! " + instrumentation.toString());

		// TODO: H�r.  F�rs�k kunna mocka f�r att se att alarmet scheduleras.  Alarmet skall sedan anv�ndas f�r att ge en notifiering
		// Hittills verkar det som att man kan schedulera larm i onCreate p� app-klassen, men eventuellt k�r denna innan man hinner mocka
		// Kanske kan onConfigurationChanged fungera...
	}
}
