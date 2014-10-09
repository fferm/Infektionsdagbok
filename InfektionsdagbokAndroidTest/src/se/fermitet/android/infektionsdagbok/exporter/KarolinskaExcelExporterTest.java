package se.fermitet.android.infektionsdagbok.exporter;

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

		assertEquals("Column 0 width", 2792, sheet.getColumnWidth(0));

		for (int colIdx = 1; colIdx <=54; colIdx++) {
			assertEquals("Column " + colIdx + " width", 480, sheet.getColumnWidth(colIdx));
		}
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
