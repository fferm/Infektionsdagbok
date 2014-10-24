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
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class ExportActivity extends InfektionsdagbokActivity<ExportView> implements OnExportCommandListener {

	Workbook wb;

	public ExportActivity() {
		super(R.layout.export_view);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);

			view.setOnExportCommandListener(this);

			setViewYearsToShow();

			getActionBar().setDisplayHomeAsUpEnabled(true);
		} catch (Exception e) {
			view.handleException(e);
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
	public void onExportCommand(int year, String name, String ssn) {
		KarolinskaExcelExporter kee = new KarolinskaExcelExporter();
		ModelManager mm = getLocalApplication().getModelManager();
		Storage storage = getLocalApplication().getStorage();
		EmailHandler email = getLocalApplication().getEmailHandler();

		try {
			wb = kee.createWorkbook(year, mm, name, ssn);
			File file = storage.sendWorkbookToFile(wb, year);
			if (file != null) {
				email.sendEmail(file, this);
			}
		} catch (Exception e) {
			view.handleException(e);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
