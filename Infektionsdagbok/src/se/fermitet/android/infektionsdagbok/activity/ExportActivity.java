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
			sendWorkbookToFile(wb, year);
			
		} catch (Exception e) {
			e.printStackTrace();
			this.notifyUserOfException(e);
		}
		
	}


	private void sendWorkbookToFile(Workbook wb, int year) {
        FileOutputStream os = null;

        try {
        	File file = new File(this.getExternalFilesDir(null), "Infektionsdagbok" + year + ".xls");
            os = new FileOutputStream(file);
            wb.write(os);

        } catch (Exception e) {
        	e.printStackTrace();
        	notifyUserOfException(e);
        } finally {
            try {
                if (os != null) os.close();
            } catch (Exception ex) {
            	ex.printStackTrace();
            	notifyUserOfException(ex);
            }
        }

	}




}
