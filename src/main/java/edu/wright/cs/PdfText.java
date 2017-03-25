package edu.wright.cs;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;

import edu.wright.cs.util.Constants;

public class PdfText {

	File pdfFileName;

	String doi;

	String pdfType;

	int firstPage;

	int lastPage;

	int noOfPages;

	public PdfText(String pdfFileName) {

		this.pdfFileName = new File(pdfFileName);
	}

	public PdfText(File pdfFileName) {
		this.pdfFileName = pdfFileName;
	}

	public String getFullText() throws InvalidPasswordException, IOException {

		return getFullText(this.pdfFileName);
	}

	private String getFullText(File pdfFileName) throws InvalidPasswordException, IOException {

		extractText(pdfFileName);
		
		return "";
	}

	private String extractText(File pdfFileName) throws InvalidPasswordException, IOException {
		System.out.println("Started");
		PDDocument doc = PDDocument.load(pdfFileName);
		noOfPages = doc.getNumberOfPages();

		System.out.println(doc.getPage(1).getContents().toString());
		return new PDFTextStripper().getText(doc);

	}

	public static void main(String[] args) {
		PdfText pdfText = new PdfText(Constants.testPdfName);
		try {

			pdfText.getFullText();
		} catch (InvalidPasswordException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
