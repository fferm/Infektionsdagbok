package se.fermitet.android.infektionsdagbok;

import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;

public class InjectionSpike extends ActivityInstrumentationTestCase2<Questionnaire> {

//	private Questionnaire activity;

	public InjectionSpike() {
		super(Questionnaire.class);
	}

	private Intent createIntent() {
		Intent i = new Intent();
		i.putExtras(getBundle());
		return i;
	}

	public class SpikeIntent extends Intent {
		public SpikeIntent() {
			super();
			this.putExtras(getBundle());
		}
	}

	@Override
	protected void setUp() throws Exception {
		System.out.println("!!!! setup started");
		super.setUp();
		super.setActivityIntent(createIntent());


		System.out.println("!!!! after launch");

	}

	private Bundle getBundle() {
		Bundle b = new Bundle();
		b.putString("my key", "my value");


		return b;
	}


	public void testHello() throws Exception {
		System.out.println("!!!! testHello started");
		System.out.println("!!!! app class: " + getActivity().getApplication().getClass().getName());
	}

	public void testHello2() throws Exception {
		System.out.println("!!!! testHello2 started");
		System.out.println("!!!! app class: " + getActivity().getApplication().getClass().getName());
	}

}
