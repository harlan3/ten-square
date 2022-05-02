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

package splitpdfs;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;

public class SplitPdfFile {

	String pdfSourceDir = "../mergepdfs/";
	String pdfDestDir   = "split_pdfs";
	
	int fromPage = 1;
	int toPage = 100;
	
	public void proccessPdf(String sourcePdf) {

		try {

			File pdfFile = new File(pdfSourceDir + File.separator + sourcePdf);
			PDDocument document = PDDocument.load(pdfFile);
			
			Splitter splitter = new Splitter();

			splitter.setStartPage(fromPage);
			splitter.setEndPage(document.getNumberOfPages());
			splitter.setSplitAtPage(toPage-fromPage+1);

			List<PDDocument> splitList = splitter.split(document);

			int i=1;
			for (PDDocument partialDoc : splitList) {		
				partialDoc.save(pdfDestDir + File.separator + "mosaic-" + i + ".pdf");
	            i++;
			}

	        document.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		SplitPdfFile splitPdfFile = new SplitPdfFile();
		splitPdfFile.proccessPdf("mergedPdf.pdf");
	}
}
