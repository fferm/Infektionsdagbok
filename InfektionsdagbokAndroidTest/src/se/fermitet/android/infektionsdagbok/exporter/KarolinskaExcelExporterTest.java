package se.fermitet.android.infektionsdagbok.exporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.LocalDate;

import se.fermitet.android.infektionsdagbok.model.ModelManager;
import se.fermitet.android.infektionsdagbok.model.ModelManagerTest_WeekAnswers;
import se.fermitet.android.infektionsdagbok.model.SickDay;
import se.fermitet.android.infektionsdagbok.model.Treatment;
import se.fermitet.android.infektionsdagbok.model.Week;
import se.fermitet.android.infektionsdagbok.model.WeekAnswers;
import se.fermitet.android.infektionsdagbok.storage.Storage;
import android.test.AndroidTestCase;

public class KarolinskaExcelExporterTest extends AndroidTestCase {
	private KarolinskaExcelExporter kee = null;
	private int year = 2014;
	private int year53 = 2009;

	private Map<Week, WeekAnswers> testData;
	private Map<Week, WeekAnswers> testData53;

	private ModelManager mm;

	private Sheet sheet;
	private Sheet sheet53;

	private String TESTNAME = "TESTNAME";
	private String TESTSSN = "123456-7890";
	private Storage storage;
	private List<SickDay> testSickDays;
	private List<Treatment> testTreatments;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		this.kee = new KarolinskaExcelExporter();

		this.storage = new Storage(getContext());
		this.mm = new ModelManager(storage);

		this.testData = ModelManagerTest_WeekAnswers.prepareTestDataIndexedByWeek(year);
		mm.saveWeekAnswers(this.testData.values());

		this.testData53 = ModelManagerTest_WeekAnswers.prepareTestDataIndexedByWeek(year53);
		mm.saveWeekAnswers(this.testData53.values());

		testSickDays = prepareTestSickDays();
		testTreatments = prepareTestTreatments();

		this.sheet = getSheet(year);
		this.sheet53 = getSheet(year53);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		this.mm.reset();
	}

	public void testSendWorkbookToFileSoThatItCanBeShown() throws Exception {
		storage.sendWorkbookToFile(createWorkbook(year), year);
	}

	public void testWorkbookHasWorksheet() throws Exception {
		assertNotNull("Has to find a worksheet", sheet);
	}

	public void testAnswers() throws Exception {
		for (int weeknum = 1; weeknum <= 52; weeknum++) {
			Week week = new Week(year, weeknum);

			WeekAnswers wa = testData.get(week);

			if (wa == null) {
				checkAnswerColumnGrayedOut(weeknum);
			} else {
				checkAnswerColumnData(weeknum, wa, year);
			}
		}
	}

	private void checkAnswerColumnGrayedOut(int weeknum) {
		for (int rowIdx = 5; rowIdx <= 14; rowIdx++) {
			Row row = sheet.getRow(rowIdx);
			Cell cell = row.getCell(weeknum);

			String value = cell.getStringCellValue();
			assertTrue("Row: " + rowIdx + " Col: " + weeknum + " should be empty", value == null || value.length() == 0);

			CellStyle style = cell.getCellStyle();
			assertEquals("Row: " + rowIdx + " Col: " + weeknum + " should be grayed out", IndexedColors.GREY_25_PERCENT.index, style.getFillForegroundColor());
		}
	}

	private void checkAnswerColumnData(int weeknum, WeekAnswers wa, int year) {
		for (int rowIdx = 5; rowIdx <= 14; rowIdx++) {
			Row row = sheet.getRow(rowIdx);
			Cell cell = row.getCell(weeknum);

			boolean answer = getAnswerFromWeekAnswers(rowIdx, wa);

			if (answer == true) {
				assertEquals("Row: " + rowIdx + " Col: " + weeknum + " contents", "X", cell.getStringCellValue());
			} else {
				String value = cell.getStringCellValue();
				assertTrue("Row: " + rowIdx + " Col: " + weeknum + " should be empty", value == null || value.length() == 0);
			}
		}
	}

	private boolean getAnswerFromWeekAnswers(int rowIdx, WeekAnswers wa) {
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
		} else if (rowIdx == 14){
			return wa.getGenerallyWell();
		} else {
			return false;
		}
	}

	public void testCellSizes() throws Exception {
		assertEquals("Column 0 width", 3258, sheet.getColumnWidth(0));

		for (int colIdx = 1; colIdx <= 52; colIdx++) {
			assertEquals("On 52 week sheet.  Column " + colIdx + " width", 504, sheet.getColumnWidth(colIdx));
		}

		for (int colIdx = 1; colIdx <= 53; colIdx++) {
			assertEquals("On 53 week sheet.  Column " + colIdx + " width", 504, sheet53.getColumnWidth(colIdx));
		}

		checkRowHeight(0, 8);
		checkRowHeight(1, 16);
		checkRowHeight(2, 16);
		for (int idx = 3; idx <= 15; idx++) {
			checkRowHeight(idx, 13);
		}

		for (int idx = 17; idx <= 25; idx++) {
			checkRowHeight(idx, 24);
		}
	}

	private void checkRowHeight(int idx, int points) {
		try {
			assertEquals("Row " + idx + " height", points * 20, sheet.getRow(idx).getHeight());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception on rowIndex " + idx);
		}
	}

	public void testPrint() throws Exception {
		assertTrue("Landscape should be true", sheet.getPrintSetup().getLandscape());
		assertTrue("Autobreaks", sheet.getAutobreaks());
		assertEquals("Fit horizontal", 1, sheet.getPrintSetup().getFitWidth());
		assertEquals("Fit vertical", 1, sheet.getPrintSetup().getFitHeight());
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
			// 53 week
			Row row = sheet53.getRow(4);
			for (i = 1; i <= 53; i++) {
				Cell cell = row.getCell(i);
				assertEquals("Cell value for week " + i, (double) i, cell.getNumericCellValue());
			}
			checkColumnEmpty(sheet53, "Column 54 should be empty on a 53 week sheet", 4, 54);

			// 52 week
			row = sheet.getRow(4);
			for (i = 1; i <= 52; i++) {
				Cell cell = row.getCell(i);
				assertEquals("Cell value for week " + i, (double) i, cell.getNumericCellValue());
			}
			checkColumnEmpty(sheet, "Column 53 should be empty on a 52 week sheet", 4, 53);
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

	public void testPersonHeaders() throws Exception {
		Row row1 = sheet.getRow(1);
		Row row2 = sheet.getRow(2);

		Cell nameHeaderCell = row1.getCell(1);
		assertEquals("Name header cell text", "Namn", nameHeaderCell.getStringCellValue());

		Cell nameCell = row1.getCell(10);
		assertEquals("Name cell text", TESTNAME, nameCell.getStringCellValue());

		Cell ssnHeaderCell = row2.getCell(1);
		assertEquals("SSN header cell text", "Personnummer", ssnHeaderCell.getStringCellValue());

		Cell ssnCell = row2.getCell(10);
		assertEquals("SSN cell text", TESTSSN, ssnCell.getStringCellValue());

		Cell headerCell = row1.getCell(30);
		assertEquals("Header cell text", "Infektionsdagbok", headerCell.getStringCellValue());
	}

	public void testBottomHeaders() throws Exception {
		Row row16 = sheet.getRow(16);

		Cell treatmentHeaderCell = row16.getCell(0);
		assertEquals("Treatment header cell text", "Antibiotikabehandling", treatmentHeaderCell.getStringCellValue());

		Cell sickDayHeaderCell = row16.getCell(33);
		assertEquals("Sick day header cell text", "Sjukskrivning", sickDayHeaderCell.getStringCellValue());

		Row row17 = sheet.getRow(17);

		assertEquals("First infectionType", "Typ av infektion", row17.getCell(0).getStringCellValue());
		assertEquals("First medicine", "Preparat", row17.getCell(1).getStringCellValue());
		assertEquals("First startingDate", "Insatt datum", row17.getCell(6).getStringCellValue());
		assertEquals("First numDays", "Dgr", row17.getCell(11).getStringCellValue());

		assertEquals("Second infectionType", "Typ av infektion", row17.getCell(13).getStringCellValue());
		assertEquals("Second medicine", "Preparat", row17.getCell(21).getStringCellValue());
		assertEquals("Second startingDate", "Insatt datum", row17.getCell(26).getStringCellValue());
		assertEquals("Second numDays", "Dgr", row17.getCell(31).getStringCellValue());

		assertEquals("First from", "Från", row17.getCell(33).getStringCellValue());
		assertEquals("First to", "Till", row17.getCell(38).getStringCellValue());
		assertEquals("Second from", "Från", row17.getCell(43).getStringCellValue());
		assertEquals("Second to", "Till", row17.getCell(48).getStringCellValue());
	}

	public void testTreatmentContents() throws Exception {
		Collections.sort(testTreatments, new Comparator<Treatment>() {
			@Override
			public int compare(Treatment lhs, Treatment rhs) {
				if (lhs.getStartingDate().isBefore(rhs.getStartingDate())) return -1;
				else return 1;
			}
		});

		int[] rowsToSearch 			= {18, 19, 20, 21, 22, 23, 24, 25, 18, 19, 20, 21, 22, 23, 24, 25};
		int[] infColsToSearch 		= {0,  0,  0,  0,  0,  0,  0,  0,  13, 13, 13, 13, 13, 13, 13, 13};
		int[] medColsToSearch		= {1,  1,  1,  1,  1,  1,  1,  1,  21, 21, 21, 21, 21, 21, 21, 21};
		int[] dateColsToSearch		= {6,  6,  6,  6,  6,  6,  6,  6,  26, 26, 26, 26, 26, 26, 26, 26};
		int[] numDaysColsToSearch	= {11, 11, 11, 11, 11, 11, 11, 11, 31, 31, 31, 31, 31, 31, 31, 31};

		assertTrue("More test data than space in excel", testTreatments.size() <= rowsToSearch.length);

		for (int i = 0; i < rowsToSearch.length && i < testTreatments.size(); i++) {
			Treatment treatment = testTreatments.get(i);
			int rowIdx = rowsToSearch[i];
			int infCol = infColsToSearch[i];
			int medCol = medColsToSearch[i];
			int dateCol = dateColsToSearch[i];
			int numDaysCol = numDaysColsToSearch[i];

			Row row = sheet.getRow(rowIdx);
			Cell infCell = row.getCell(infCol);
			Cell medCell = row.getCell(medCol);
			Cell dateCell = row.getCell(dateCol);
			Cell numDaysCell = row.getCell(numDaysCol);

			if (treatment.getStartingDate() == null) {
				assertTrue("Row: " + rowIdx + " col: " + dateCol + " date null", dateCell.getStringCellValue() == null || dateCell.getStringCellValue().length() == 0);
			} else {
				assertEquals("Row: " + rowIdx + " col: " + dateCol + " date value", treatment.getStartingDate().toDate(), dateCell.getDateCellValue());
			}

			if (treatment.getNumDays() == null) {
				assertTrue("Row: " + rowIdx + " col: " + numDaysCol + " numDays null", numDaysCell.getStringCellValue() == null || numDaysCell.getStringCellValue().length() == 0);
			} else {
				assertEquals("Row: " + rowIdx + " col: " + numDaysCol + " numDays value", (double) treatment.getNumDays(), numDaysCell.getNumericCellValue());
			}

			if (treatment.getInfectionType() == null) {
				assertTrue("Row: " + rowIdx + " col: " + infCol + " infectionType null", infCell.getStringCellValue() == null || infCell.getStringCellValue().length() == 0);
			} else {
				assertEquals("Row: " + rowIdx + " col: " + infCol + " infectionType value", treatment.getInfectionType(), infCell.getStringCellValue());
			}

			if (treatment.getMedicine() == null) {
				assertTrue("Row: " + rowIdx + " col: " + medCol + " medicine null", medCell.getStringCellValue() == null || medCell.getStringCellValue().length() == 0);
			} else {
				assertEquals("Row: " + rowIdx + " col: " + medCol + " medicine value", treatment.getMedicine(), medCell.getStringCellValue());
			}
		}
	}

	private List<Treatment> prepareTestTreatments() throws Exception{
		List<Treatment> testTreatments = new ArrayList<Treatment>();

		testTreatments.add(new Treatment(new LocalDate(year, 1, 1), 1, "INFECTION 1", "MEDICINE 1"));
		testTreatments.add(new Treatment(new LocalDate(year, 2, 2), 2, "INFECTION 2", "MEDICINE 2"));
		testTreatments.add(new Treatment(new LocalDate(year, 3, 3), 3, "INFECTION 3", "MEDICINE 3"));
		testTreatments.add(new Treatment(new LocalDate(year, 4, 4), 4, "INFECTION 4", "MEDICINE 4"));
		testTreatments.add(new Treatment(new LocalDate(year, 5, 5), 5, "INFECTION 5", "MEDICINE 5"));
		testTreatments.add(new Treatment(new LocalDate(year, 6, 6), 6, "INFECTION 6", "MEDICINE 6"));
		testTreatments.add(new Treatment(new LocalDate(year, 7, 7), 7, "INFECTION 7", "MEDICINE 7"));
		testTreatments.add(new Treatment(new LocalDate(year, 8, 8), 8, "INFECTION 8", "MEDICINE 8"));
		testTreatments.add(new Treatment(new LocalDate(year, 9, 9), null, "INFECTION 9", "MEDICINE 9"));
		testTreatments.add(new Treatment(new LocalDate(year, 10, 10), 10, null, "MEDICINE 10"));
		testTreatments.add(new Treatment(new LocalDate(year, 11, 11), 11, "INFECTION 11", null));

		Collections.shuffle(testTreatments);  // Shuffle to ensure sorting must be done
		mm.saveAll(testTreatments);
		return testTreatments;
	}

	public void testSickDayContents() throws Exception {
		Collections.sort(testSickDays, new Comparator<SickDay>() {
			@Override
			public int compare(SickDay lhs, SickDay rhs) {
				LocalDate lhsDate = lhs.getStart();
				LocalDate rhsDate = rhs.getStart();

				if (lhsDate == null) return 1;
				if (rhsDate == null) return -1;

				if (lhsDate.isBefore(rhsDate)) return -1;
				else if (lhsDate.equals(rhsDate)) return 0;
				else return 1;
			}
		});

		int[] rowsToSearch 			= {18, 19, 20, 21, 22, 23, 24, 25, 18, 19, 20, 21, 22, 23, 24, 25};
		int[] fromColsToSearch 		= {33, 33, 33, 33, 33, 33, 33, 33, 43, 43, 43, 43, 43, 43, 43, 43};
		int[] toColsToSearch		= {38, 38, 38, 38, 38, 38, 38, 38, 48, 48, 48, 48, 48, 48, 48, 48};

		assertTrue("More test data than space in excel", testSickDays.size() <= rowsToSearch.length);

		for (int i = 0; i < rowsToSearch.length && i < testSickDays.size(); i++) {
			SickDay item = testSickDays.get(i);
			int rowIdx = rowsToSearch[i];
			int fromCol = fromColsToSearch[i];
			int toCol = toColsToSearch[i];

			Row row = sheet.getRow(rowIdx);
			Cell fromCell = row.getCell(fromCol);
			Cell toCell = row.getCell(toCol);

			if (item.getStart() == null) {
				assertTrue("Row: " + rowIdx + " col: " + fromCol + " from date null", fromCell.getStringCellValue() == null || fromCell.getStringCellValue().length() == 0);
			} else {
				assertEquals("Row: " + rowIdx + " col: " + fromCol + " from date value", item.getStart().toDate(), fromCell.getDateCellValue());
			}

			if (item.getEnd() == null) {
				assertTrue("Row: " + rowIdx + " col: " + toCol + " to date null", toCell.getStringCellValue() == null || toCell.getStringCellValue().length() == 0);
			} else {
				assertEquals("Row: " + rowIdx + " col: " + toCol + " to date value", item.getEnd().toDate(), toCell.getDateCellValue());
			}
		}
	}


	private List<SickDay> prepareTestSickDays() throws Exception{
		List<SickDay> testSickDays= new ArrayList<SickDay>();

		testSickDays.add(new SickDay(new LocalDate(year, 1, 1), new LocalDate(year, 1, 2)));
		testSickDays.add(new SickDay(new LocalDate(year, 2, 1), new LocalDate(year, 2, 2)));
		testSickDays.add(new SickDay(new LocalDate(year, 3, 1), new LocalDate(year, 3, 2)));
		testSickDays.add(new SickDay(new LocalDate(year, 4, 1), new LocalDate(year, 4, 2)));
		testSickDays.add(new SickDay(new LocalDate(year, 5, 1), new LocalDate(year, 5, 2)));
		testSickDays.add(new SickDay(new LocalDate(year, 6, 1), new LocalDate(year, 6, 2)));
		testSickDays.add(new SickDay(new LocalDate(year, 7, 1), new LocalDate(year, 7, 2)));
		testSickDays.add(new SickDay(new LocalDate(year, 8, 1), new LocalDate(year, 8, 2)));
		testSickDays.add(new SickDay(null, new LocalDate(year, 9, 2)));
		testSickDays.add(new SickDay(new LocalDate(year, 10, 1), null));

		Collections.shuffle(testSickDays);  // Shuffle to ensure sorting must be done
		mm.saveAll(testSickDays);
		return testSickDays;
	}



	public void testYearNumber() throws Exception {
		assertEquals("Year text", (double) year, sheet.getRow(3).getCell(1).getNumericCellValue());
	}

	protected Sheet getSheet(int year) throws Exception {
		Workbook wb = createWorkbook(year);
		String nameToLookFor = "Infektionsdagbok";

		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			Sheet sheet = wb.getSheetAt(i);
			if (sheet.getSheetName().equals(nameToLookFor)) {
				return sheet;
			}
		}
		return null;
	}

	private Workbook createWorkbook(int year) throws Exception {
		Workbook wb = kee.createWorkbook(year, this.mm, TESTNAME, TESTSSN);
		return wb;
	}

}
