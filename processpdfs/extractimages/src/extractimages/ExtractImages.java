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

	// Generated image sizes must not exceed GL_MAX_TEXTURE_SIZE limit (typical 16384 per side)
	double resizeRatio = 0.49;
	
	public String padLeftZeros(String inputString, int length) {

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

					BufferedImage sourceImage = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
					String fileName = extractImages.imgDir + File.separator + "images"
							+ extractImages.padLeftZeros(Integer.toString(count), 5) + ".png";
					BufferedImage resizedImage = Scalr.resize(sourceImage, Method.QUALITY, Mode.FIT_TO_WIDTH,
							(int) (sourceImage.getWidth() * extractImages.resizeRatio), org.imgscalr.Scalr.OP_ANTIALIAS);
					ImageIO.write(resizedImage, "png", new File(fileName));
					//System.out.println("Saving: " + fileName);
					count++;
				}
				document.close();
			} catch (IOException e) {
				System.err.println("Exception while trying to create pdf document - " + e);
			}
		}
	}
}
