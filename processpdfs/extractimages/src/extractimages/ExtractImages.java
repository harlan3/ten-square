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

package extractimages;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.contentstream.PDFStreamEngine;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;

public class ExtractImages extends PDFStreamEngine {

	String pdfDir = "../splitpdfs/split_pdfs";
	String imgDir = "../images";

	// Generated image sizes must not exceed GL_MAX_TEXTURE_SIZE limit (typical
	// 16384 per side)
	double targetTextureSide = 15500.0;

	private String padLeftZeros(String inputString, int length) {

		if (inputString.length() >= length) {
			return inputString;
		}
		StringBuilder sb = new StringBuilder();
		while (sb.length() < length - inputString.length()) {
			sb.append('0');
		}
		sb.append(inputString);

		return sb.toString();
	}

	private double calculateResizeRatio(float width) {

		double sideDim;
		double totalPixelWidth;
		double resizeRatio;

		sideDim = width * (1.0 / 72.0); // 1 pt = 1/72 inch
		totalPixelWidth = sideDim * 300.0 * 10.0;
		resizeRatio = targetTextureSide / totalPixelWidth;
		resizeRatio = Math.floor(resizeRatio * 100) / 100;

		return resizeRatio;
	}

	public static void main(String[] args) throws IOException {

		ExtractImages extractImages = new ExtractImages();

		File dir = new File(extractImages.pdfDir);
		File[] directoryListing = dir.listFiles();
		java.util.Arrays.sort(directoryListing);

		int count = 1;

		for (File pdfFile : directoryListing) {

			System.out.println("Working on pdf: " + pdfFile.getName());

			try (final PDDocument document = PDDocument.load(pdfFile)) {

				PDFRenderer pdfRenderer = new PDFRenderer(document);

				double resizeRatio = extractImages.calculateResizeRatio(document.getPage(1).getMediaBox().getHeight());

				for (int page = 0; page < document.getNumberOfPages(); ++page) {

					BufferedImage sourceImage = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
					String fileName = extractImages.imgDir + File.separator + "images"
							+ extractImages.padLeftZeros(Integer.toString(count), 5) + ".png";
					BufferedImage resizedImage = Scalr.resize(sourceImage, Method.QUALITY, Mode.FIT_TO_WIDTH,
							(int) (sourceImage.getWidth() * resizeRatio), org.imgscalr.Scalr.OP_ANTIALIAS);
					ImageIO.write(resizedImage, "png", new File(fileName));
					// System.out.println("Saving: " + fileName);
					count++;
				}
				document.close();
			} catch (IOException e) {
				System.err.println("Exception while trying to create pdf document - " + e);
			}
		}
	}
}
