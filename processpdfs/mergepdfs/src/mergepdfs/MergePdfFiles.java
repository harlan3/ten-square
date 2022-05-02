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

package mergepdfs;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;

public class MergePdfFiles {

	String pdfDir = "../../source_pdfs";

	public void proccessPdf(String mergedFileName) {

		try {

			PDFMergerUtility pdfmerger = new PDFMergerUtility();
			File dir = new File(pdfDir);
			File[] directoryListing = dir.listFiles();

			if (directoryListing != null) {

				for (File pdfFile : directoryListing) {

			            PDDocument document = PDDocument.load(pdfFile);
			            pdfmerger.setDestinationFileName(mergedFileName);
			            pdfmerger.addSource(pdfFile);
			            pdfmerger.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());
			            document.close();
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		MergePdfFiles mergePdfFiles = new MergePdfFiles();
		mergePdfFiles.proccessPdf("mergedPdf.pdf");
	}
}
