package se.fermitet.android.infektionsdagbok.spikes;

import org.joda.time.DateTime;

public class JodaSpike {
	public static void main(String[] args) {
		DateTime dt = new DateTime();
		System.out.println("Short text: " + dt.weekOfWeekyear().getAsShortText());
		System.out.println("String: " + dt.weekOfWeekyear().getAsString());
		System.out.println("Text: " + dt.weekOfWeekyear().getAsText());
	}

}
