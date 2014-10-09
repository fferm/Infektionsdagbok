package se.fermitet.android.infektionsdagbok.exporter;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;

import android.test.AndroidTestCase;

public class KarolinskaExcelExporterTest extends AndroidTestCase {

	public void testWorkbookHasWorksheet() throws Exception {
		KarolinskaExcelExporter kee = new KarolinskaExcelExporter(getContext());

		String nameToLookFor = "Infektionsdagbok";

		//Workbook wb = kee.export(model, DateTime.now().withDayOfYear(1), DateTime.now().withDayOfYear(365));
		Workbook wb = kee.export(DateTime.now().withDayOfYear(1), DateTime.now().withDayOfYear(365));

		assertNotNull("Workbook is null", wb);

		boolean found = false;
		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			Sheet sheet = wb.getSheetAt(i);
			if (sheet.getSheetName().equals(nameToLookFor)) {
				found = true;
				break;
			}
		}

		assertTrue("Has to find a worksheet with the name " + nameToLookFor, found);
		assertNotNull(kee);
	}
}
