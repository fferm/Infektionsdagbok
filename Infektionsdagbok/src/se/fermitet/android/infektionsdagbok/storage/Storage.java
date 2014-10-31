package se.fermitet.android.infektionsdagbok.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONException;

import se.fermitet.android.infektionsdagbok.model.ModelObjectBase;
import se.fermitet.android.infektionsdagbok.model.SickDay;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import se.fermitet.android.infektionsdagbok.model.Week;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;
import android.content.Context;

public class Storage {

	private static final String ANSWER_FILE_EXTENSION = "answer";
	private static final String TREATMENT_FILE_NAME = "treatments.json";
	private static final String SICK_DAY_FILE_NAME = "sickDays.json";
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

			pw.println(Jsonizer.weekAnswersToJSON(toSave));
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
			return Jsonizer.weekAnswersFromJSON(json);
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

	public Map<UUID, Treatment> getAllTreatments() throws Exception {
		Map<UUID, Treatment> ret = new HashMap<UUID, Treatment>();

		Map<UUID, ModelObjectBase> fromRead = performRead(TREATMENT_FILE_NAME, new JSONCaller<Treatment>() {
			@Override
			public Treatment objectFromJSON(String jsonTxt) throws JSONException {
				return Jsonizer.treatmentFromJSON(jsonTxt);
			}
		});

		for (UUID key : fromRead.keySet()) {
			ret.put(key, (Treatment) fromRead.get(key));
		}

		return ret;
	}

	public void saveTreatments(Collection<Treatment> treatments) throws Exception {
		performSave(treatments, TREATMENT_FILE_NAME);
	}

	public Map<UUID, SickDay> getAllSickDays() throws Exception {
		Map<UUID, SickDay> ret = new HashMap<UUID, SickDay>();

		Map<UUID, ModelObjectBase> fromRead = performRead(SICK_DAY_FILE_NAME, new JSONCaller<SickDay>() {
			@Override
			public SickDay objectFromJSON(String jsonTxt) throws JSONException {
				return Jsonizer.sickDayFromJSON(jsonTxt);
			}
		});

		for (UUID key : fromRead.keySet()) {
			ret.put(key, (SickDay) fromRead.get(key));
		}

		return ret;
	}

	public void saveSickDays(Collection<SickDay> toSave) throws Exception {
		performSave(toSave, SICK_DAY_FILE_NAME);
	}

	private void performSave(Collection<? extends ModelObjectBase> objs, String fileName) throws Exception {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(this.context.openFileOutput(fileName, Context.MODE_PRIVATE));

			for (ModelObjectBase obj : objs) {
				pw.println(Jsonizer.modelObjectToJSON(obj));
			}

		} finally {
			if (pw != null) {
				pw.flush();
				pw.close();
			}
		}
	}

	private interface JSONCaller<T extends ModelObjectBase> {
		public T objectFromJSON(String jsonTxt) throws JSONException;
	}


	public Map<UUID, ModelObjectBase> performRead(String fileName, JSONCaller<? extends ModelObjectBase> caller) throws Exception {
		Map<UUID, ModelObjectBase> ret = new HashMap<UUID, ModelObjectBase>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(this.context.openFileInput(fileName)));

			String json;
			while ((json = br.readLine()) != null) {
				ModelObjectBase obj = caller.objectFromJSON(json);
				ret.put(obj.getUUID(), obj);
			}

			return ret;
		} catch (FileNotFoundException e) {
			// File does not exist yet, do nothing...
			return ret;
		} finally {
			if (br != null) br.close();
		}
	}




}
