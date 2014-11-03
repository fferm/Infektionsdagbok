package se.fermitet.android.infektionsdagbok.exporter;

import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import se.fermitet.android.infektionsdagbok.model.ModelManager;
import se.fermitet.android.infektionsdagbok.model.Week;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;

public class KarolinskaExcelExporter {

	private Font verdana21Bold;
	private Font verdana12Bold;
	private Font verdana12Normal;
	private Font verdana10Bold;
	private Font verdana10Normal;
	private Font verdana8Bold;
	private Font verdana8Normal;
	private Font verdana6Bold;
	private Font verdana6Normal;

	private int weeksInYear;

	public KarolinskaExcelExporter() {
		super();
	}

	public Workbook createWorkbook(int year, ModelManager mm, String name, String ssn) throws Exception {
		weeksInYear = Week.weeksInTheYear(year);
		Workbook wb = new HSSFWorkbook();
		createFonts(wb);

		Sheet sheet = wb.createSheet("Infektionsdagbok");
		setSheetGlobalParameters(sheet);
		setColumnWidths(sheet);

		addRows(sheet);
		writePersonHeaders(sheet, wb, name, ssn);
		writeYear(sheet, wb, year);
		writeRowHeaders(sheet, wb);
		writeWeekHeaders(sheet, wb);
		writeAnswers(sheet, wb, mm, year);

		handleTreatments(sheet, wb, mm, year);
		handleSickDays(sheet, wb, mm, year);
		Workbook ret = wb;

		return ret;
	}


	private void createFonts(Workbook wb) {
		this.verdana21Bold 		= createFont(wb, 21, Font.BOLDWEIGHT_BOLD);
		this.verdana12Bold 		= createFont(wb, 12, Font.BOLDWEIGHT_BOLD);
		this.verdana12Normal 	= createFont(wb, 12, Font.BOLDWEIGHT_NORMAL);
		this.verdana10Normal 	= createFont(wb, 10, Font.BOLDWEIGHT_NORMAL);
		this.verdana10Bold 		= createFont(wb, 10, Font.BOLDWEIGHT_BOLD);
		this.verdana8Bold 		= createFont(wb, 8, Font.BOLDWEIGHT_BOLD);
		this.verdana8Normal 	= createFont(wb, 8, Font.BOLDWEIGHT_NORMAL);
		this.verdana6Bold 		= createFont(wb, 6, Font.BOLDWEIGHT_BOLD);
		this.verdana6Normal 	= createFont(wb, 6, Font.BOLDWEIGHT_NORMAL);
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

		for (int colIdx = 1; colIdx <= weeksInYear; colIdx++) {
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

		for (int idx = 3; idx <= 16; idx++) {
			sheet.createRow(idx);
		}

		for (int idx = 17; idx <= 25; idx++) {
			Row row = sheet.createRow(idx);
			row.setHeight((short) 480);
		}
	}

	private void writePersonHeaders(Sheet sheet, Workbook wb, String name, String ssn) {
		writeNameHeader(sheet, wb);
		writeNameValue(sheet, wb, name);
		writeSSNHeader(sheet, wb);
		writeSSNValue(sheet, wb, ssn);
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

	private void writeNameValue(Sheet sheet, Workbook wb, String name) {
		for (int col = 10; col <= 19; col++) {
			createCell(sheet, wb, 1, col, null, this.verdana12Normal,
					(col == 10 ? CellStyle.BORDER_THIN : CellStyle.BORDER_NONE),
					(col == 19 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_NONE),
					CellStyle.BORDER_MEDIUM,
					CellStyle.BORDER_NONE);
		}
		sheet.getRow(1).getCell(10).setCellValue(name);

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

	private void writeSSNValue(Sheet sheet, Workbook wb, String ssn) {
		for (int col = 10; col <= 19; col++) {
			createCell(sheet, wb, 2, col, null, this.verdana12Normal,
					(col == 10 ? CellStyle.BORDER_THIN : CellStyle.BORDER_NONE),
					(col == 19 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_NONE),
					CellStyle.BORDER_NONE,
					CellStyle.BORDER_MEDIUM);
		}
		sheet.getRow(2).getCell(10).setCellValue(ssn);

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
		for (int col = 1; col <= weeksInYear; col++) {
			createCell(sheet, wb, 3, col, null, this.verdana8Bold,
					(col == 1 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_NONE),
					(col == weeksInYear ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_NONE),
					CellStyle.BORDER_MEDIUM,
					CellStyle.BORDER_MEDIUM);
		}
		Cell cell = sheet.getRow(3).getCell(1);

		cell.getCellStyle().setAlignment(CellStyle.ALIGN_CENTER);
		cell.setCellValue(year);

		sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, weeksInYear));
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
		for (int weeknum = 1; weeknum <= weeksInYear; weeknum++) {

			Cell cell = createCell(sheet, wb, 4, weeknum, null, this.verdana6Normal,
					(weeknum == 1 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN),
					(weeknum == weeksInYear ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN),
					(CellStyle.BORDER_MEDIUM),
					(CellStyle.BORDER_MEDIUM));

			cell.getCellStyle().setAlignment(CellStyle.ALIGN_CENTER);

			cell.setCellValue(weeknum);
		}
	}

	private void writeAnswers(Sheet sheet, Workbook wb, ModelManager mm, int year) throws Exception {
		Map<Week, WeekAnswers> weekAnswers = mm.getAllWeekAnswersInYear(year);

		for (int weeknum = 1; weeknum <= weeksInYear; weeknum++) {
			Week wk = new Week(year, weeknum);
			WeekAnswers wa = weekAnswers.get(wk);

			for (int rowIdx = 5; rowIdx <= 14; rowIdx++) {

				Cell cell = createCell(sheet, wb, rowIdx, weeknum, null, this.verdana10Normal,
						(weeknum == 1 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN),
						(weeknum == weeksInYear ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN),
						(rowIdx == 5 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN),
						(rowIdx == 14 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN));

				CellStyle style = cell.getCellStyle();

				style.setAlignment(CellStyle.ALIGN_CENTER);

				if (wa == null) {
					style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
					style.setFillPattern(CellStyle.SOLID_FOREGROUND);
				} else {
					if (getSingleAnswer(wa, rowIdx) == true) {
						cell.setCellValue("X");
					}
				}
			}
		}
	}

	private boolean getSingleAnswer(WeekAnswers wa, int rowIdx) {
		if (rowIdx == 5) {
			return wa.getMalaise();
		} else if (rowIdx == 6) {
			return wa.getFever();
		} else if (rowIdx == 7) {
			return wa.getEarAche();
		} else if (rowIdx == 8) {
			return wa.getSoreThroat();
		} else if (rowIdx == 9) {
			return wa.getRunnyNose();
		} else if (rowIdx == 10) {
			return wa.getStommacAche();
		} else if (rowIdx == 11) {
			return wa.getDryCough();
		} else if (rowIdx == 12) {
			return wa.getWetCough();
		} else if (rowIdx == 13) {
			return wa.getMorningCough();
		} else if (rowIdx == 14) {
			return wa.getGenerallyWell();
		} else {
			return false;
		}
	}

	private void handleTreatments(Sheet sheet, Workbook wb, ModelManager mm, int year) {
		writeTreatmentTopHeader(sheet, wb);
		writeTreatmentCellsWithoutText(sheet, wb, 0, 1, 6, 11, 12);
		writeTreatmentCellsWithoutText(sheet, wb, 13, 21, 26, 31, 32);
	}

	private void handleSickDays(Sheet sheet, Workbook wb, ModelManager mm, int year) {
		writeSickDayTopHeader(sheet, wb);
		writeSickDaysCellsWithoutText(sheet, wb, 33, 38, 42);
		writeSickDaysCellsWithoutText(sheet, wb, 43, 48, 52);
	}

	private void writeTreatmentTopHeader(Sheet sheet, Workbook wb) {
		for (int col = 0; col <= 32; col++) {
			Cell cell = createCell(sheet, wb, 16, col, null, this.verdana10Bold,
					(col == 0 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_NONE),
					(col == 32 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_NONE),
					CellStyle.BORDER_MEDIUM,
					CellStyle.BORDER_MEDIUM);
			cell.getCellStyle().setAlignment(CellStyle.ALIGN_CENTER);

		}
		sheet.getRow(16).getCell(0).setCellValue("Antibiotikabehandling");

		sheet.addMergedRegion(new CellRangeAddress(16, 16, 0, 32));
	}

	private void writeTreatmentCellsWithoutText(Sheet sheet, Workbook wb, int infTypeLeft, int medicineLeft, int dateLeft, int numDaysLeft, int numDaysRight) {
		for (int row = 17; row <= 25; row++) {
			for (int col = infTypeLeft; col <= numDaysRight; col++) {
				Cell cell = createCell(sheet, wb, row, col, null,
						(row == 17 ? verdana8Bold : verdana8Normal),
						(col == infTypeLeft ? CellStyle.BORDER_MEDIUM : (col == medicineLeft || col == dateLeft || col == numDaysLeft ? CellStyle.BORDER_THIN : CellStyle.BORDER_NONE)),
						(col == numDaysRight ? CellStyle.BORDER_MEDIUM : (col == (medicineLeft - 1) || col == (dateLeft - 1) || col == (numDaysLeft - 1) ? CellStyle.BORDER_THIN : CellStyle.BORDER_NONE)),
						(row == 17 || row == 18 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN),
						(row == 17 || row == 25 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN));
				cell.getCellStyle().setVerticalAlignment(CellStyle.VERTICAL_TOP);
				cell.getCellStyle().setWrapText(true);
			}
			sheet.addMergedRegion(new CellRangeAddress(row, row, infTypeLeft, medicineLeft - 1));
			sheet.addMergedRegion(new CellRangeAddress(row, row, medicineLeft, dateLeft - 1));
			sheet.addMergedRegion(new CellRangeAddress(row, row, dateLeft, numDaysLeft - 1));
			sheet.addMergedRegion(new CellRangeAddress(row, row, numDaysLeft, numDaysRight));

		}
		sheet.getRow(17).getCell(infTypeLeft).setCellValue("Typ av infektion");
		sheet.getRow(17).getCell(medicineLeft).setCellValue("Preparat");
		sheet.getRow(17).getCell(dateLeft).setCellValue("Insatt datum");
		sheet.getRow(17).getCell(numDaysLeft).setCellValue("Dgr");
	}

	private void writeSickDaysCellsWithoutText(Sheet sheet, Workbook wb, int startLeft, int endLeft, int endRight) {
		for (int row = 17; row <= 25; row++) {
			for (int col = startLeft; col <= endRight; col++) {
				Cell cell = createCell(sheet, wb, row, col, null,
						(row == 17 ? verdana8Bold : verdana8Normal),
						(col == startLeft ? CellStyle.BORDER_MEDIUM : (col == endLeft ? CellStyle.BORDER_THIN : CellStyle.BORDER_NONE)),
						(col == endRight ? CellStyle.BORDER_MEDIUM : (col == (endLeft - 1) ? CellStyle.BORDER_THIN : CellStyle.BORDER_NONE)),
						(row == 17 || row == 18 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN),
						(row == 17 || row == 25 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_THIN));
				cell.getCellStyle().setAlignment(CellStyle.ALIGN_CENTER);
				cell.getCellStyle().setVerticalAlignment(CellStyle.VERTICAL_TOP);
				cell.getCellStyle().setWrapText(true);
			}
			sheet.addMergedRegion(new CellRangeAddress(row, row, startLeft, endLeft - 1));
			sheet.addMergedRegion(new CellRangeAddress(row, row, endLeft, endRight));
		}
		sheet.getRow(17).getCell(startLeft).setCellValue("Från");
		sheet.getRow(17).getCell(endLeft).setCellValue("Till");
	}




	private void writeSickDayTopHeader(Sheet sheet, Workbook wb) {
		for (int col = 33; col <= 52; col++) {
			Cell cell = createCell(sheet, wb, 16, col, null, this.verdana10Bold,
					(col == 33 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_NONE),
					(col == 52 ? CellStyle.BORDER_MEDIUM : CellStyle.BORDER_NONE),
					CellStyle.BORDER_MEDIUM,
					CellStyle.BORDER_MEDIUM);
			cell.getCellStyle().setAlignment(CellStyle.ALIGN_CENTER);

		}
		sheet.getRow(16).getCell(33).setCellValue("Sjukskrivning");

		sheet.addMergedRegion(new CellRangeAddress(16, 16, 33, 52));
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

}
