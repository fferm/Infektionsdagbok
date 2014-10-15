package se.fermitet.android.infektionsdagbok.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.exporter.KarolinskaExcelExporter;
import se.fermitet.android.infektionsdagbok.model.ModelManager;
import se.fermitet.android.infektionsdagbok.model.Week;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;
import se.fermitet.android.infektionsdagbok.storage.EmailHandler;
import se.fermitet.android.infektionsdagbok.storage.Storage;
import se.fermitet.android.infektionsdagbok.views.ExportView;
import se.fermitet.android.infektionsdagbok.views.ExportView.OnExportCommandListener;
import android.os.Bundle;

public class ExportActivity extends InfektionsdagbokActivity<ExportView> implements OnExportCommandListener {

	public ExportActivity() {
		super(R.layout.export_view);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		try {
			super.onCreate(savedInstanceState);

			view.setOnExportCommandListener(this);

			setViewYearsToShow();
		} catch (Exception e) {
			this.handleException(e);
		}
	}

	public void setViewYearsToShow() throws Exception {
		List<Integer> years = new ArrayList<Integer>();

		ModelManager mm = getLocalApplication().getModelManager();
		WeekAnswers firstWeekAnswers = mm.getEarliestWeekAnswers();

		if (firstWeekAnswers == null) {
			years.add(DateTime.now().weekyear().get());
		} else {
			Week firstWeekOfAnswers = firstWeekAnswers.week;
			int firstYearToShow = firstWeekOfAnswers.year();
			int currentYear = DateTime.now().year().get();

			for (int year = firstYearToShow; year <= currentYear; year++) {
				years.add(year);
			}
		}
		Collections.sort(years, Collections.reverseOrder());

		view.setYearsToShow(years);
	}

	@Override
	public void onExportCommand(int year) {
		KarolinskaExcelExporter kee = new KarolinskaExcelExporter(this);
		ModelManager mm = getLocalApplication().getModelManager();
		Storage storage = getLocalApplication().getStorage();
		EmailHandler email = getLocalApplication().getEmailHandler();

		try {
			Workbook wb = kee.export(year, mm);
			File file = storage.sendWorkbookToFile(wb, year);
			if (file != null) {
				email.sendEmail(file, this);
			}
		} catch (Exception e) {
			this.handleException(e);
		}

	}
}
