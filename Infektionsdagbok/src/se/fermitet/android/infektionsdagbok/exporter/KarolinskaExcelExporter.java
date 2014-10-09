package se.fermitet.android.infektionsdagbok.exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;

import android.content.Context;

public class KarolinskaExcelExporter {

	private Context context;

	public KarolinskaExcelExporter(Context context) {
		super();
		this.context = context;
	}

	public Workbook export(DateTime startDate, DateTime endDate) {
		Workbook ret = createWorkbook(startDate, endDate);

		sendWorkbookToFile(ret);

		return ret;
	}

	private Workbook createWorkbook(DateTime startDate, DateTime endDate) {
        Workbook wb = new HSSFWorkbook();

        wb.createSheet("Infektionsdagbok");

        return wb;
	}

	private void sendWorkbookToFile(Workbook wb) {
        // Create a path where we will place our List of objects on external storage
        File file = new File(context.getExternalFilesDir(null), "Infektionsdagbok.xls");
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            System.out.println("!!!! Writing file " + file);
        } catch (IOException e) {
        	e.printStackTrace();
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
            try {
                if (null != os) os.close();
            } catch (Exception ex) {
            	ex.printStackTrace();
            }
        }

	}


/*
	private boolean saveExcelFile() {
		String fileName = "Infektionsdagbok.xls";

        // check if available and not read only
       if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e(TAG, "Storage not available or read only");
            return false;
        }

        boolean success = false;

        //New Workbook
        Workbook wb = new HSSFWorkbook();

        Cell c = null;

        //Cell style for header row
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIME.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("myOrder");

        // Generate column headings
        Row row = sheet1.createRow(0);

        c = row.createCell(0);
        c.setCellValue("Item Number");
        c.setCellStyle(cs);

        c = row.createCell(1);
        c.setCellValue("Quantity");
        c.setCellStyle(cs);

        c = row.createCell(2);
        c.setCellValue("Price");
        c.setCellStyle(cs);

        sheet1.setColumnWidth(0, (15 * 500));
        sheet1.setColumnWidth(1, (15 * 500));
        sheet1.setColumnWidth(2, (15 * 500));

        // Create a path where we will place our List of objects on external storage
        File file = new File(this.getExternalFilesDir(null), fileName);
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            System.out.println("!!!! Writing file " + file);
            success = true;
        } catch (IOException e) {
        	e.printStackTrace();
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
            try {
                if (null != os) os.close();

                this.notifyUserWithMessage("Fil klar");
            } catch (Exception ex) {
            	ex.printStackTrace();
            }
        }
        return success;
    }


*/
}
