package se.fermitet.android.infektionsdagbok;

public class NameFromIdHelper {
	public static String getNameFromId(int id) {
		if (id == R.id.malaise) return "malaise";
		if (id == R.id.fever) return "fever";
		if (id == R.id.earAche) return "earAche";
		if (id == R.id.soreThroat) return "soreThroat";
		if (id == R.id.runnyNose) return "runnyNose";
		if (id == R.id.stommacAche) return "stommacAche";
		if (id == R.id.dryCough) return "dryCough";
		if (id == R.id.wetCough) return "wetCough";
		if (id == R.id.morningCough) return "morningCough";
		if (id == R.id.generallyWell) return "generallyWell";

		return "unknown";
	}

}
