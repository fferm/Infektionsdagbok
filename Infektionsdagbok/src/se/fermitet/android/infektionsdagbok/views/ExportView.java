package se.fermitet.android.infektionsdagbok.views;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;

import se.fermitet.android.infektionsdagbok.R;
import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ExportView extends RelativeLayout implements View.OnClickListener {

	private TextView startDateTV;
	private TextView endDateTV;
	private Button exportBTN;
	private DateTime startDate;
	private DateTime endDate;

	public ExportView(Context context, AttributeSet attrs) {
		super(context, attrs);

		startDate = DateTime.now().withDayOfYear(1);
		endDate = DateTime.now();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		attachWidgets();
		setupWidgets();
	}

	private void attachWidgets() {
		startDateTV = (TextView) findViewById(R.id.startDateTV);
		endDateTV = (TextView) findViewById(R.id.endDateTV);
		exportBTN = (Button) findViewById(R.id.exportBTN);
	}

	private void setupWidgets() {
		startDateTV.setOnClickListener(this);
		endDateTV.setOnClickListener(this);
		exportBTN.setOnClickListener(this);
		syncDateTexts();
	}


	public DateTime getStartDate() {
		return startDate;
	}
	
	private void setStartDate(DateTime date) {
		this.startDate = date;
		syncDateTexts();
	}

	public DateTime getEndDate() {
		return endDate;
	}
	
	private void setEndDate(DateTime date) {
		this.endDate = date;
		syncDateTexts();
	}

	@Override
	public void onClick(View v) {
		if (v == startDateTV || v == endDateTV) {
			handleDateClicks(v);
		} else if (v == exportBTN) {
			handleExportButtonClick();
		}
	}

	private void handleDateClicks(View v) {
		DateTime initial = null;
		DatePickerDialog.OnDateSetListener listener = null;

		if (v == startDateTV) {
			initial = getStartDate();
			listener = new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					ExportView.this.setStartDate(new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0));
				}
			};
		} else if (v == endDateTV) {
			initial = getEndDate();
			listener = new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					ExportView.this.setEndDate(new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0));
				}
			};
		}

		DatePickerDialog picker = new DatePickerDialog(getContext(), listener, initial.year().get(), initial.monthOfYear().get() - 1, initial.dayOfMonth().get());
		picker.show();
	}

	private void syncDateTexts() {
		DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);

		startDateTV.setText(format.format(startDate.toDate()));
		endDateTV.setText(format.format(endDate.toDate()));
	}

	private void handleExportButtonClick() {
		if (getStartDate().isAfter(getEndDate())) {
			String msg = "Kan inte ha startdatum efter slutdatum";
			Toast toast = Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT);
			toast.show();
			return;
		}
		
		System.out.println("!!!! file write success is " + saveExcelFile());
	}

	private boolean saveExcelFile() { 
		Context context = getContext();
		String fileName = "Infektionsdagbok.xls";
		
        // check if available and not read only 
 /*       if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) { 
            Log.e(TAG, "Storage not available or read only"); 
            return false; 
        }*/ 
 
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
        File file = new File(context.getExternalFilesDir(null), fileName); 
        FileOutputStream os = null; 
 
        try { 
            os = new FileOutputStream(file);
            wb.write(os);
//            Log.w("FileUtils", "Writing file" + file); 
            success = true; 
        } catch (IOException e) { 
  //          Log.w("FileUtils", "Error writing " + file, e); 
        } catch (Exception e) { 
    //        Log.w("FileUtils", "Failed to save file", e); 
        } finally { 
            try { 
                if (null != os) 
                    os.close(); 
            } catch (Exception ex) { 
            } 
        } 
        return success; 
    } 
}
