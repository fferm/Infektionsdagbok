package se.fermitet.android.infektionsdagbok.app;

import android.app.Instrumentation;
import android.test.InstrumentationTestCase;

public class AlarmTest extends InstrumentationTestCase {
	public void testALaunch() throws Exception {
		Instrumentation instrumentation = this.getInstrumentation();
		System.out.println("!!!! " + instrumentation.toString());

		// TODO: Här.  Försök kunna mocka för att se att alarmet scheduleras.  Alarmet skall sedan användas för att ge en notifiering
		// Hittills verkar det som att man kan schedulera larm i onCreate på app-klassen, men eventuellt kör denna innan man hinner mocka
		// Kanske kan onConfigurationChanged fungera...
	}
}
