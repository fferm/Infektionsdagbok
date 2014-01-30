package se.fermitet.android.infektionsdagbok;

import java.lang.reflect.Field;

import android.util.SparseArray;

public class NameFromIdHelper {
	private static SparseArray<String> fieldNamesById;

	static {
		fieldNamesById = new SparseArray<String>();

		try {
			Class<?>[] rClasses = R.class.getClasses();
			for (int i = 0; i < rClasses.length; i++) {
				Class<?> innerClass = rClasses[i];
				Field[] fields = innerClass.getFields();
				for (int j = 0; j < fields.length; j++) {
					Field field = fields[j];
					int value = field.getInt(null);  // null since it is a static field

					fieldNamesById.put(value, field.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getNameFromId(int id) {
		return fieldNamesById.get(id);
	}

}
