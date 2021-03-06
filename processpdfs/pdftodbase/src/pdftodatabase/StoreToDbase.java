/*
 * Ten Square (OpenGL Decagonal Prism Surface Viewer)
 * Copyright (c) 2022 Harlan Murphy - Orbisoftware
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
 
package pdftodatabase;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.util.regex.Matcher;

public class StoreToDbase {

	String pdfDir = "../splitpdfs/split_pdfs";
	DatabaseAPI databaseAPI = new DatabaseAPI();
	
	public void proccessPdf() {

		try {

			File dir = new File(pdfDir);
			File[] directoryListing = dir.listFiles();
			java.util.Arrays.sort(directoryListing);
			
			if (directoryListing != null) {

				int tableIndex = 1;
				
				for (File pdfFile : directoryListing) {
					
					PDDocument pdDoc;
					pdDoc = PDDocument.load(pdfFile);

					int pageCount = pdDoc.getNumberOfPages();

					PDFTextStripper stripper = new PDFTextStripper();

					for (int pageIndex = 1; pageIndex <= pageCount; pageIndex++) {
						stripper.setStartPage(pageIndex);
						stripper.setEndPage(pageIndex);
						String text = stripper.getText(pdDoc).replaceAll("'", "''");
						databaseAPI.insertPageMetaData(tableIndex, pageIndex, text);
					}
					
					pdDoc.close();
					tableIndex++;
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void searchResultsTest() {

		String keyWord = "old";

		Pattern p = Pattern.compile("((?U)\\w+)\\W+(\\w+)\\W+(\\w+)\\W+" + keyWord + "\\W+(\\w+)\\W+(\\w+)\\W+(\\w+)");
		Matcher m = p.matcher(databaseAPI.retrievePageText(1, 3));

		while (m.find())
			System.out.println(m.group(1) + " " + m.group(2) + " " + m.group(3) + " " + keyWord + " " +
					m.group(4) + " " + m.group(5) + " " + m.group(6));
		
	}

	public static void main(String[] args) {

		StoreToDbase storeToDbase = new StoreToDbase();
		int numberTables = new File(storeToDbase.pdfDir).listFiles().length;
		
		storeToDbase.databaseAPI.initDatabase();
		storeToDbase.databaseAPI.createTables(numberTables);
		storeToDbase.proccessPdf();
	}
}
