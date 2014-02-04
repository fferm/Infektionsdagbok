package se.fermitet.android.infektionsdagbok.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.json.JSONException;

import se.fermitet.android.infektionsdagbok.model.Week;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;
import android.content.Context;

public class Storage {

	private Context context;

	public Storage(Context context) {
		super();
		this.context = context;
	}

	// TODO: Who handles exceptions?
	public WeekAnswers getAnswersForWeek(Week week) throws IOException, JSONException {
		WeekAnswers ret = null;
		BufferedReader br = null;
		try {
			if (fileExists(week)) {
				br = new BufferedReader(new InputStreamReader(this.context.openFileInput(getFilename(week))));

				String json = br.readLine();

				ret = WeekAnswers.fromJSON(json);
			}
			return ret;
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}

	// TODO: Who handles exceptions?
	public void saveAnswers(WeekAnswers toSave) throws IOException, JSONException {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(this.context.openFileOutput(getFilename(toSave.week), Context.MODE_PRIVATE));

			pw.println(toSave.toJSON());
		} finally {
			if (pw != null) {
				pw.flush();
				pw.close();
			}
		}
	}

	public void clear() {
		File[] files = this.context.getFilesDir().listFiles();

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			file.delete();
		}
	}


	private boolean fileExists(Week week) {
		File[] files = this.context.getFilesDir().listFiles();

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.getName().equals(getFilename(week))) {
				return true;
			}
		}
		return false;
	}



	private String getFilename(Week week) {
		return week.toString() + ".json";
	}

}
