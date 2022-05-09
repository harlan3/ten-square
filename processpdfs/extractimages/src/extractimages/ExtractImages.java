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

import java.awt.Graphics;
import java.awt.Image;
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
	double targetTextureSide = 16384.0;
	float targetDPI = 300;
	int targetWidth = (int) (targetTextureSide / 10.0);
	int targetHeight = (int) (targetTextureSide / 10.0);

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

				for (int page = 0; page < document.getNumberOfPages(); ++page) {

					BufferedImage sourceImage = pdfRenderer.renderImageWithDPI(page, extractImages.targetDPI,
							ImageType.RGB);
					Image scaledImage = sourceImage.getScaledInstance(extractImages.targetWidth,
							extractImages.targetHeight, Image.SCALE_SMOOTH);
					sourceImage.getGraphics().drawImage(scaledImage, 0, 0, null);

					int type = BufferedImage.TYPE_INT_RGB;
					BufferedImage scaledBufferedImage = new BufferedImage(scaledImage.getWidth(null),
							scaledImage.getHeight(null), type);
					Graphics g = scaledBufferedImage.createGraphics();
					g.drawImage(scaledImage, 0, 0, null);
					g.dispose();

					String fileName = extractImages.imgDir + File.separator + "images"
							+ extractImages.padLeftZeros(Integer.toString(count), 5) + ".png";
					ImageIO.write(scaledBufferedImage, "png", new File(fileName));
					count++;
				}
				document.close();
			} catch (IOException e) {
				System.err.println("Exception while trying to create pdf document - " + e);
			}
		}
	}
}
