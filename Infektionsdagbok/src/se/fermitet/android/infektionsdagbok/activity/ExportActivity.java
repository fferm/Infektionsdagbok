package se.fermitet.android.infektionsdagbok.activity;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.R;
import se.fermitet.android.infektionsdagbok.exporter.KarolinskaExcelExporter;
import se.fermitet.android.infektionsdagbok.model.ModelManager;
import se.fermitet.android.infektionsdagbok.views.ExportView;
import se.fermitet.android.infektionsdagbok.views.ExportView.OnExportCommandListener;
import android.content.Intent;
import android.net.Uri;
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
		
		// TODO: Hantera att det skall vara rätt år
		int year = 2014;
		
		try {
			Workbook wb = kee.export(year, mm);
			File file = sendWorkbookToFile(wb, year);
			if (file != null) {
				sendEmail(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.notifyUserOfException(e);
		}
		
	}

	private File sendWorkbookToFile(Workbook wb, int year) throws Exception {
        FileOutputStream os = null;

        try {
        	File file = new File(this.getExternalFilesDir(null), "Infektionsdagbok" + year + ".xls");
            os = new FileOutputStream(file);
            wb.write(os);

            return file;
        } finally {
            if (os != null) os.close();
        }

	}

	private void sendEmail(File file) {
		String email = "fredrik@fermitet.se";
		String subject = "Infektionsdagbok - test";
		String message = "Testmeddelande";
		
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		
		emailIntent.setType("application/vnd.ms-excel");
		
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		
		emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
		
		this.startActivity(emailIntent);
	}
}
