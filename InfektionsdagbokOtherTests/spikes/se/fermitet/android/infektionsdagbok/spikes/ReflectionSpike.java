package se.fermitet.android.infektionsdagbok.spikes;

import java.lang.reflect.Field;

import se.fermitet.android.infektionsdagbok.R;

public class ReflectionSpike {

	public static void main(String[] args) {
		ReflectionSpike r = new ReflectionSpike();
		try {
			r.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void run() throws IllegalArgumentException, IllegalAccessException {
		Class<R> rClass = R.class;

		System.out.println("!!!! hej");
		Class<?>[] classes = rClass.getClasses();
		for (int i = 0; i < classes.length; i++) {
			Class<?> innerClass = classes[i];
			System.out.println("!!!! inner class: " + innerClass.getName());

			Field[] fields = innerClass.getFields();
			for (int j = 0; j < fields.length; j++) {
				Field field = fields[j];
				System.out.println("!!!! fields: " + field.getName() + "    " + field.getInt(null));
			}
		}

	}

}
