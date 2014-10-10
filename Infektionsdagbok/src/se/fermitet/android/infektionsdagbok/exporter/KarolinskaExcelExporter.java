package se.fermitet.android.infektionsdagbok.exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import se.fermitet.android.infektionsdagbok.model.ModelManager;
import se.fermitet.android.infektionsdagbok.model.Week;
import android.content.Context;

public class KarolinskaExcelExporter {

	private Context context;

	private Font verdana21Bold;
	private Font verdana12Bold;
	private Font verdana12Normal;
	private Font verdana10Normal;
	private Font verdana8Bold;
	private Font verdana6Bold;
	private Font verdana6Normal;

	private int numWeeks;

	public KarolinskaExcelExporter(Context context) {
		super();
		this.context = context;
	}

	public Workbook export(int year, ModelManager mm) {
		numWeeks = checkNumWeeks(year);

		System.out.println("!!!! Year: " + year + "  weeks: " + numWeeks);

		Workbook ret = createWorkbook(year, mm);

		sendWorkbookToFile(ret, year);

		return ret;
	}

	private int checkNumWeeks(int year) {
		Week week = new Week(year, 52);
		Week next = week.next();
		if (next.year() == year) {
			return 53;
		} else {
			return 52;
		}
	}

	private Workbook createWorkbook(int year, ModelManager mm) {
        Workbook wb = new HSSFWorkbook();
        createFonts(wb);

        Sheet diarySheet = wb.createSheet("Infektionsdagbok");
        setSheetGlobalParameters(diarySheet);
        setColumnWidths(diarySheet);

        addRows(diarySheet);
        writeHeaders(diarySheet, wb);
        writeYear(diarySheet, wb, year);
        writeRowHeaders(diarySheet, wb);
        writeWeekHeaders(diarySheet, wb);
        writeAnswers(diarySheet, wb);

        return wb;
	}

	private void createFonts(Workbook wb) {
		this.verdana21Bold = createFont(wb, 21, Font.BOLDWEIGHT_BOLD);
		this.verdana12Bold = createFont(wb, 12, Font.BOLDWEIGHT_BOLD);
		this.verdana12Normal = createFont(wb, 12, Font.BOLDWEIGHT_NORMAL);
		this.verdana10Normal = createFont(wb, 10, Font.BOLDWEIGHT_NORMAL);
		this.verdana8Bold = createFont(wb, 8, Font.BOLDWEIGHT_BOLD);
		this.verdana6Bold = createFont(wb, 6, Font.BOLDWEIGHT_BOLD);
		this.verdana6Normal = createFont(wb, 6, Font.BOLDWEIGHT_NORMAL);
	}

	private Font createFont(Workbook wb, int points, short bold) {
		Font font = wb.createFont();
		font.setFontName("Verdana");
		font.setFontHeightInPoints((short) points);
		font.setBoldweight(bold);

		return font;
	}

	private void setSheetGlobalParameters(Sheet sheet) {
		sheet.setDefaultRowHeight((short) 260);
		sheet.getPrintSetup().setLandscape(true);

		sheet.setAutobreaks(true);

	    sheet.getPrintSetup().setFitHeight((short)1);
	    sheet.getPrintSetup().setFitWidth((short)1);
	}

	private void setColumnWidths(Sheet sheet) {
		sheet.setColumnWidth(0, 3258);

		for (int colIdx = 1; colIdx <= 53; colIdx++) {
			sheet.setColumnWidth(colIdx, 504);
		}
	}

	private void addRows(Sheet sheet) {
		Row row0 = sheet.createRow(0);
		row0.setHeight((short) 160);

		Row row1 = sheet.createRow(1);
		row1.setHeight((short) 320);

		Row row2 = sheet.createRow(2);
		row2.setHeight((short) 320);

		for (int idx = 3; idx <=14; idx++) {
			sheet.createRow(idx);
		}
	}

	private void writeHeaders(Sheet sheet, Workbook wb) {
		writeNameHeader(sheet, wb);
		writeNameValue(sheet, wb);
		writeSSNHeader(sheet, wb);
		writeSSNValue(sheet, wb);
		writeHeader(sheet, wb);
	}

	private void writeNameHeader(Sheet sheet, Workbook wb) {
		for (int col = 1; col <= 9; col++) {
			createCell(sheet, wb, 1, col, null, this.verdana12Bold,
					(col == 1 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_NONE),
					(col == 9 ? CellStyle.BORDER_THIN : CellStyle.BORDER_NONE),
					CellStyle.BORDER_MEDIUM,
					CellStyle.BORDER_NONE);
		}
		sheet.getRow(1).getCell(1).setCellValue("Namn");

		sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 9));
	}

	private void writeNameValue(Sheet sheet, Workbook wb) {
		for (int col = 10; col <= 19; col++) {
			createCell(sheet, wb, 1, col, null, this.verdana12Normal,
					(col == 10 ? CellStyle.BORDER_THIN : CellStyle.BORDER_NONE),
					(col == 19 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_NONE),
					CellStyle.BORDER_MEDIUM,
					CellStyle.BORDER_NONE);
		}
		sheet.getRow(1).getCell(10).setCellValue("Kalle Persson");

		sheet.addMergedRegion(new CellRangeAddress(1, 1, 10, 19));
	}

	private void writeSSNHeader(Sheet sheet, Workbook wb) {
		for (int col = 1; col <= 9; col++) {
			createCell(sheet, wb, 2, col, null, this.verdana12Bold,
					(col == 1 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_NONE),
					(col == 9 ? CellStyle.BORDER_THIN : CellStyle.BORDER_NONE),
					CellStyle.BORDER_NONE,
					CellStyle.BORDER_MEDIUM);
		}
		sheet.getRow(2).getCell(1).setCellValue("Personnummer");

		sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 9));
	}

	private void writeSSNValue(Sheet sheet, Workbook wb) {
		for (int col = 10; col <= 19; col++) {
			createCell(sheet, wb, 2, col, null, this.verdana12Normal,
					(col == 10 ? CellStyle.BORDER_THIN : CellStyle.BORDER_NONE),
					(col == 19 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_NONE),
					CellStyle.BORDER_NONE,
					CellStyle.BORDER_MEDIUM);
		}
		sheet.getRow(2).getCell(10).setCellValue("123456-7890");

		sheet.addMergedRegion(new CellRangeAddress(2, 2, 10, 19));
	}

	private void writeHeader(Sheet sheet, Workbook wb) {
		for (int rowIdx = 1; rowIdx <= 2; rowIdx++) {
			for (int col = 30; col <= 48; col++) {
				Cell cell = createCell(sheet, wb, rowIdx, col, null, this.verdana21Bold,
						CellStyle.BORDER_NONE,
						CellStyle.BORDER_NONE,
						CellStyle.BORDER_NONE,
						CellStyle.BORDER_NONE);

				cell.getCellStyle().setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			}
		}
		sheet.getRow(1).getCell(30).setCellValue("Infektionsdagbok");

		sheet.addMergedRegion(new CellRangeAddress(1, 2, 30, 48));
	}

	private void writeYear(Sheet sheet, Workbook wb, int year) {
		for (int col = 1; col <= 53; col++) {
			createCell(sheet, wb, 3, col, null, this.verdana8Bold,
					(col == 1 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_NONE),
					(col == 53 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_NONE),
					CellStyle.BORDER_MEDIUM,
					CellStyle.BORDER_MEDIUM);
		}
		Cell cell = sheet.getRow(3).getCell(1);

		cell.getCellStyle().setAlignment(CellStyle.ALIGN_CENTER);
		cell.setCellValue(year);

		sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 53));
	}

	private void writeRowHeaders(Sheet sheet, Workbook wb) {
		writeRowHeader(sheet, wb, 5, "Sjukdomskänsla");
		writeRowHeader(sheet, wb, 6, "Feber > 38");
		writeRowHeader(sheet, wb, 7, "Öronvärk");
		writeRowHeader(sheet, wb, 8, "Halsont");
		writeRowHeader(sheet, wb, 9, "Snuva");
		writeRowHeader(sheet, wb, 10, "Magbesvär");
		writeRowHeader(sheet, wb, 11, "Torrhosta");
		writeRowHeader(sheet, wb, 12, "Slemhosta");
		writeRowHeader(sheet, wb, 13, "Morgonupphostning");
		writeRowHeader(sheet, wb, 14, "Väsentligen frisk");
	}

	private void writeRowHeader(Sheet sheet, Workbook wb, int rowIdx, String txt) {
		createCell(sheet, wb, rowIdx, 0, txt, this.verdana6Bold,
				(CellStyle.BORDER_MEDIUM),
				(CellStyle.BORDER_MEDIUM),
				(rowIdx == 5 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN),
				(rowIdx == 14 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN));
	}

	private void writeWeekHeaders(Sheet sheet, Workbook wb) {
		for (int weeknum = 1; weeknum <= 53; weeknum++) {

			Cell cell = createCell(sheet, wb, 4, weeknum, null, this.verdana6Normal,
					(weeknum == 1 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN),
					(weeknum == 53 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN),
					(CellStyle.BORDER_MEDIUM),
					(CellStyle.BORDER_MEDIUM));

			cell.getCellStyle().setAlignment(CellStyle.ALIGN_CENTER);

			cell.setCellValue(weeknum);
		}
	}

	private void writeAnswers(Sheet sheet, Workbook wb) {
		for (int rowIdx = 5; rowIdx <= 14; rowIdx++) {
			for (int weeknum = 1; weeknum <= 53; weeknum++) {

				Cell cell = createCell(sheet, wb, rowIdx, weeknum, null, this.verdana10Normal,
						(weeknum == 1 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN),
						(weeknum == 53 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN),
						(rowIdx == 5 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN),
						(rowIdx == 14 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN));

				cell.getCellStyle().setAlignment(CellStyle.ALIGN_CENTER);
			}
		}
	}

	private Cell createCell(Sheet sheet,
			Workbook wb,
			int rowIdx,
			int colIdx,
			String txt,
			Font font,
			short borderLeft,
			short borderRight,
			short borderTop,
			short borderBottom) {

		Row row = sheet.getRow(rowIdx);
		Cell cell = row.createCell(colIdx);

		CellStyle cs = wb.createCellStyle();
		cs.setBorderLeft(borderLeft);
		cs.setBorderRight(borderRight);
		cs.setBorderTop(borderTop);
		cs.setBorderBottom(borderBottom);
		cs.setFont(font);
		cell.setCellStyle(cs);

		if (txt != null) {
			cell.setCellValue(txt);
		}

		return cell;
	}


	private void sendWorkbookToFile(Workbook wb, int year) {
        // Create a path where we will place our List of objects on external storage
        File file = new File(context.getExternalFilesDir(null), "Infektionsdagbok" + year + ".xls");
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
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
