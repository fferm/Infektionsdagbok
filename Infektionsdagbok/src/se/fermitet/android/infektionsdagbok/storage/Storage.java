package se.fermitet.android.infektionsdagbok.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.poi.ss.usermodel.Workbook;

import se.fermitet.android.infektionsdagbok.model.Week;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;
import android.content.Context;

public class Storage {

	private static final String ANSWER_FILE_EXTENSION = "json";
	private Context context;

	public Storage(Context context) {
		super();
		this.context = context;
	}

	public WeekAnswers getAnswersForWeek(Week week) throws Exception {
		WeekAnswers ret = null;
		if (fileExists(week)) {
			ret = readAnswersFromFile(getWeekAnswersFilename(week));
		}
		return ret;
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

	public Collection<WeekAnswers> getAllAnswers() throws Exception {
		String[] fileNamesInDir = this.context.getFilesDir().list();
		Collection<WeekAnswers> ret = new ArrayList<WeekAnswers>();

		for (int i = 0; i < fileNamesInDir.length; i++) {
			String fileName = fileNamesInDir[i];

			String[] splits = fileName.split("\\.(?=[^\\.]+$)"); // http://stackoverflow.com/questions/4545937/java-splitting-the-filename-into-a-base-and-extension
			String extension = splits[splits.length - 1];

			if (extension.equals(ANSWER_FILE_EXTENSION)) {
				ret.add(readAnswersFromFile(fileName));
			}
		}

		return ret;
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

	private WeekAnswers readAnswersFromFile(String fileName) throws Exception {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(this.context.openFileInput(fileName)));
			String json = br.readLine();
			return WeekAnswers.fromJSON(json);
		} finally {
			if (br != null) br.close();
		}
	}

	private String getWeekAnswersFilename(Week week) {
		return week.toString() + "." + ANSWER_FILE_EXTENSION;
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
