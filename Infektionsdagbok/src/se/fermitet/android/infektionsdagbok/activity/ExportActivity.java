package se.fermitet.android.infektionsdagbok.activity;

import java.io.File;

import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.exporter.KarolinskaExcelExporter;
import se.fermitet.android.infektionsdagbok.model.ModelManager;
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
		super.onCreate(savedInstanceState);

		view.setOnExportCommandListener(this);
	}

	@Override
	public void onExportCommand(DateTime startDate, DateTime endDate) {
		KarolinskaExcelExporter kee = new KarolinskaExcelExporter(this);
		ModelManager mm = getLocalApplication().getModelManager();
		Storage storage = getLocalApplication().getStorage();
		EmailHandler email = getLocalApplication().getEmailHandler();

		// TODO: Hantera att det skall vara rätt år
		int year = 2014;

		try {
			Workbook wb = kee.export(year, mm);
			File file = storage.sendWorkbookToFile(wb, year);
			if (file != null) {
				email.sendEmail(file, this);
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.notifyUserOfException(e);
		}

	}
}
