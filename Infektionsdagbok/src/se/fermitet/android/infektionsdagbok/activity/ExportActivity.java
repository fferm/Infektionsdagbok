package se.fermitet.android.infektionsdagbok.activity;

import java.io.File;
import java.util.ArrayList;
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
		System.out.println("!!!! activity.onCreate() start");

		try {
			super.onCreate(savedInstanceState);

			view.setOnExportCommandListener(this);
System.out.println("!!!! before setViewYearsToShow()");
			setViewYearsToShow();
			System.out.println("!!!! activity.onCreate() end");
		} catch (Exception e) {
			this.handleException(e);
		}
	}

/*	@Override
	protected void onStart() {
		super.onStart();
		try {
			setViewYearsToShow();
		} catch (Exception e) {
			this.handleException(e);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		try {
		setViewYearsToShow();
		} catch (Exception e) {
			this.handleException(e);
		}
	}*/

	public void setViewYearsToShow() throws Exception {
		List<Integer> years = new ArrayList<Integer>();
		
		ModelManager mm = getLocalApplication().getModelManager();
		
		WeekAnswers firstWeekAnswers = mm.getEarliestWeekAnswers();
		if (firstWeekAnswers == null) {
			// TODO
			return;
		} else {
			Week firstWeekOfAnswers = firstWeekAnswers.week;
			int firstYearToShow = firstWeekOfAnswers.year();
			int currentYear = DateTime.now().year().get();
			
			for (int year = firstYearToShow; year <= currentYear; year++) {
				years.add(year);
			}
			
			view.setYearsToShow(years);
			
			System.out.println("!!!!! Activity size of array: " + years.size());
		}
	}

	@Override
	public void onExportCommand(DateTime startDate, DateTime endDate) {
		KarolinskaExcelExporter kee = new KarolinskaExcelExporter(this);
		ModelManager mm = getLocalApplication().getModelManager();
		Storage storage = getLocalApplication().getStorage();
		EmailHandler email = getLocalApplication().getEmailHandler();

		// TODO: Hantera att det skall vara rŠtt Œr
		int year = 2014;

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
