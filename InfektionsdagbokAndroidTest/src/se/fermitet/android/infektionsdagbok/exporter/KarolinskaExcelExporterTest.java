package se.fermitet.android.infektionsdagbok.exporter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;

import android.test.AndroidTestCase;

public class KarolinskaExcelExporterTest extends AndroidTestCase {
	private KarolinskaExcelExporter kee = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		kee = new KarolinskaExcelExporter(getContext());
	}

	public void testWorkbookHasWorksheet() throws Exception {
		//Workbook wb = kee.export(model, DateTime.now().withDayOfYear(1), DateTime.now().withDayOfYear(365));
		assertNotNull("Has to find a worksheet", getSheet());

	}

	public void testCellSizes() throws Exception {
		Sheet sheet = getSheet();

		assertEquals("Column 0 width", 3258, sheet.getColumnWidth(0));

		for (int colIdx = 1; colIdx <= 53; colIdx++) {
			assertEquals("Column " + colIdx + " width", 504, sheet.getColumnWidth(colIdx));
		}

		checkRowHeight(sheet, 0, 8);
		checkRowHeight(sheet, 1, 16);
		checkRowHeight(sheet, 2, 16);
		for (int idx = 3; idx <= 14; idx++) {
			checkRowHeight(sheet, idx, 13);
		}
	}

	private void checkRowHeight(Sheet sheet, int idx, int points) {
		try {
			assertEquals("Row " + idx + " height", points * 20, sheet.getRow(idx).getHeight());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception on rowIndex " + idx);
		}
	}

	public void testPrint() throws Exception {
		Sheet sheet = getSheet();

		assertTrue("Landscape should be true", sheet.getPrintSetup().getLandscape());
		assertTrue("Autobreaks", sheet.getAutobreaks());
		assertEquals("Fit horizontal", 1, sheet.getPrintSetup().getFitWidth());
		assertEquals("Fit vertical", 1, sheet.getPrintSetup().getFitHeight());
	}

	public void testRowHeaders() throws Exception {
		Sheet sheet = getSheet();

		checkRowHeader(sheet, 5, "Sjukdomskänsla");
		checkRowHeader(sheet, 6, "Feber > 38");
		checkRowHeader(sheet, 7, "Öronvärk");
		checkRowHeader(sheet, 8, "Halsont");
		checkRowHeader(sheet, 9, "Snuva");
		checkRowHeader(sheet, 10, "Magbesvär");
		checkRowHeader(sheet, 11, "Torrhosta");
		checkRowHeader(sheet, 12, "Slemhosta");
		checkRowHeader(sheet, 13, "Morgonupphostning");
		checkRowHeader(sheet, 14, "Väsentligen frisk");
	}


	private void checkRowHeader(Sheet sheet, int rowIdx, String text) {
		try {
			Row row = sheet.getRow(rowIdx);
			Cell cell = row.getCell(0);

			assertEquals("Text in row header of row " + rowIdx, text, cell.getStringCellValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception on rowIndex " + rowIdx);
		}
	}

	public void testWeekNumbers() throws Exception {
		int i = 0;
		try {
			Sheet sheet = getSheet();
			Row row = sheet.getRow(4);
			for (i = 1; i <= 53; i++) {
				Cell cell = row.getCell(i);
				assertEquals("Cell value for week " + i, (double) i, cell.getNumericCellValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception on week " + i);
		}
	}

	public void testHeaders() throws Exception {
		Sheet sheet = getSheet();
		Row row1 = sheet.getRow(1);
		Row row2 = sheet.getRow(2);

		Cell nameCell = row1.getCell(1);
		assertEquals("Name cell text", "Namn", nameCell.getStringCellValue());

		Cell ssnCell = row2.getCell(1);
		assertEquals("SSN cell text", "Personnummer", ssnCell.getStringCellValue());

		Cell headerCell = row1.getCell(30);
		assertEquals("Header cell text", "Infektionsdagbok", headerCell.getStringCellValue());
	}

	public void testYearNumber() throws Exception {
		assertEquals("Year text", (double) DateTime.now().year().get(), getSheet().getRow(3).getCell(1).getNumericCellValue());
	}

	protected Sheet getSheet() {
		Workbook wb = kee.export(DateTime.now().year().get());
		String nameToLookFor = "Infektionsdagbok";

		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			Sheet sheet = wb.getSheetAt(i);
			if (sheet.getSheetName().equals(nameToLookFor)) {
				return sheet;
			}
		}
		return null;
	}

}
