package se.fermitet.android.infektionsdagbok.exporter;

import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import se.fermitet.android.infektionsdagbok.model.ModelManager;
import se.fermitet.android.infektionsdagbok.model.ModelManagerTest;
import se.fermitet.android.infektionsdagbok.model.Week;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;
import se.fermitet.android.infektionsdagbok.storage.Storage;
import android.test.AndroidTestCase;

public class KarolinskaExcelExporterTest extends AndroidTestCase {
	private KarolinskaExcelExporter kee = null;
	private int year52 = 2014;
	private int year53 = 2009;

	private Map<Week, WeekAnswers> testData52;
	private Map<Week, WeekAnswers> testData53;

	private ModelManager mm;

	private Sheet sheet52;
	private Sheet sheet53;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		this.kee = new KarolinskaExcelExporter(getContext());

		this.mm = new ModelManager(new Storage(getContext()));

		this.testData52 = ModelManagerTest.prepareTestDataIndexedByWeek(year52);
		mm.saveWeekAnswers(this.testData52.values());

		this.testData53 = ModelManagerTest.prepareTestDataIndexedByWeek(year53);
		mm.saveWeekAnswers(this.testData53.values());

		this.sheet52 = getSheet(year52);
		this.sheet53 = getSheet(year53);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		this.mm.reset();
	}

	public void testWorkbookHasWorksheet() throws Exception {
		assertNotNull("Has to find a worksheet", sheet52);
	}

/*	public void testAnswers() throws Exception {
		for (int weeknum = 1; weeknum <= 53; weeknum++) {
			Week week = new Week(year52, weeknum);

			WeekAnswers wa = testData52.get(week);

			if (wa == null) {
				checkAnswerColumnEmpty(weeknum);
			} else {

			}
		}
	}

	private void checkAnswerColumnEmpty(int weeknum) {
		for (int rowIdx = 5; rowIdx <= 14; rowIdx++) {
			Row row = sheet.getRow(rowIdx);
			Cell cell = row.getCell(weeknum);

			String value = cell.getStringCellValue();
			assertTrue("Row: " + rowIdx + " Col: " + weeknum + " should be empty", value == null || value.length() == 0);
		}

	}*/

/*	public void testCellSizes() throws Exception {
		assertEquals("Column 0 width", 3258, sheet.getColumnWidth(0));

		for (int colIdx = 1; colIdx <= 53; colIdx++) {
			assertEquals("Column " + colIdx + " width", 504, sheet.getColumnWidth(colIdx));
		}

		checkRowHeight(0, 8);
		checkRowHeight(1, 16);
		checkRowHeight(2, 16);
		for (int idx = 3; idx <= 14; idx++) {
			checkRowHeight(idx, 13);
		}
	}*/

	private void checkRowHeight(int idx, int points) {
		try {
			assertEquals("Row " + idx + " height", points * 20, sheet52.getRow(idx).getHeight());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception on rowIndex " + idx);
		}
	}

	public void testPrint() throws Exception {
		assertTrue("Landscape should be true", sheet53.getPrintSetup().getLandscape());
		assertTrue("Autobreaks", sheet53.getAutobreaks());
		assertEquals("Fit horizontal", 1, sheet53.getPrintSetup().getFitWidth());
		assertEquals("Fit vertical", 1, sheet53.getPrintSetup().getFitHeight());
	}

	public void testRowHeaders() throws Exception {
		checkRowHeader(5, "Sjukdomskänsla");
		checkRowHeader(6, "Feber > 38");
		checkRowHeader(7, "Öronvärk");
		checkRowHeader(8, "Halsont");
		checkRowHeader(9, "Snuva");
		checkRowHeader(10, "Magbesvär");
		checkRowHeader(11, "Torrhosta");
		checkRowHeader(12, "Slemhosta");
		checkRowHeader(13, "Morgonupphostning");
		checkRowHeader(14, "Väsentligen frisk");
	}


	private void checkRowHeader(int rowIdx, String text) {
		try {
			Row row = sheet52.getRow(rowIdx);
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
			// 53 week
			Row row = sheet53.getRow(4);
			for (i = 1; i <= 53; i++) {
				Cell cell = row.getCell(i);
				assertEquals("Cell value for week " + i, (double) i, cell.getNumericCellValue());
			}
			checkColumnEmpty(sheet53, "Column 54 should be empty on a 53 week sheet", 4, 54);

			// 52 week
			row = sheet52.getRow(4);
			for (i = 1; i <= 52; i++) {
				Cell cell = row.getCell(i);
				assertEquals("Cell value for week " + i, (double) i, cell.getNumericCellValue());
			}
			checkColumnEmpty(sheet52, "Column 53 should be empty on a 52 week sheet", 4, 53);
		} catch (NullPointerException e) {
			e.printStackTrace();
			fail("Exception on week " + i);
		}
	}

	private void checkColumnEmpty(Sheet sheet, String msg, int rowIdx, int colIdx) {
		try {
			Cell cell = sheet.getRow(rowIdx).getCell(colIdx);

			String value = cell.getStringCellValue();
			assertTrue(msg, value == null || value.length() == 0);
		} catch (NullPointerException ex) {
			// OK
		}
	}

	public void testHeaders() throws Exception {
		Row row1 = sheet52.getRow(1);
		Row row2 = sheet53.getRow(2);

		Cell nameCell = row1.getCell(1);
		assertEquals("Name cell text", "Namn", nameCell.getStringCellValue());

		Cell ssnCell = row2.getCell(1);
		assertEquals("SSN cell text", "Personnummer", ssnCell.getStringCellValue());

		Cell headerCell = row1.getCell(30);
		assertEquals("Header cell text", "Infektionsdagbok", headerCell.getStringCellValue());
	}

	public void testYearNumber() throws Exception {
		assertEquals("Year text", (double) year52, sheet52.getRow(3).getCell(1).getNumericCellValue());
	}

	protected Sheet getSheet(int year) {
		Workbook wb = kee.export(year, this.mm);
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
