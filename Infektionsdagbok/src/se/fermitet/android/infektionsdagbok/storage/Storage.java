package se.fermitet.android.infektionsdagbok.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.apache.poi.ss.usermodel.Workbook;

import se.fermitet.android.infektionsdagbok.model.Week;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;
import android.content.Context;

public class Storage {

	private Context context;

	public Storage(Context context) {
		super();
		this.context = context;
	}

	public WeekAnswers getAnswersForWeek(Week week) throws Exception {
		WeekAnswers ret = null;
		BufferedReader br = null;
		try {
			if (fileExists(week)) {
				br = new BufferedReader(new InputStreamReader(this.context.openFileInput(getWeekAnswersFilename(week))));

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

	public void saveAnswers(WeekAnswers toSave) throws Exception {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(this.context.openFileOutput(getWeekAnswersFilename(toSave.week), Context.MODE_PRIVATE));

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
			if (file.getName().equals(getWeekAnswersFilename(week))) {
				return true;
			}
		}
		return false;
	}

	private String getWeekAnswersFilename(Week week) {
		return week.toString() + ".json";
	}

	public File sendWorkbookToFile(Workbook wb, int year) throws Exception {
        FileOutputStream os = null;

        try {
        	File file = new File(this.context.getExternalFilesDir(null), "Infektionsdagbok" + year + ".xls");
            os = new FileOutputStream(file);
            wb.write(os);

            return file;
        } finally {
            if (os != null) os.close();
        }
	}



}
