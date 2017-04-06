/**
 * 
 */
package edu.wright.cs;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

/**
 * 
 * @author Apoorva
 *
 */

public class PublisherYear {

	PDDocument doc;
	String fullText;

	public enum months {
		JANUARY, JAN, FEBRUARY, FEB, MARCH, MAR, APRIL, APR, MAY, JUNE, JUN, JULY, JUL, AUGUST, AUG, SEPTEMBER, SEP, OCTOBER, OCT, NOVEMBER, NOV, DECEMBER, DEC
	}

	public enum yearSearchTerms {
		PUBLISHED, PUBLISH, PUBLICATION, AVAILABLE, PUBLISHER
	}
	// NOT SURE IF "ACCEPTED" should be included.

	public enum publisherList {
		IEEE, ACM, ELSEVIER, SPRINGER
	}

	public static boolean isMonthNear(String[] words, int i) {
		if (i > 0) {
			for (Month month : Month.values()) {
				if (words[i - 1].equalsIgnoreCase(month.toString()))

					return true;
			}
		}
		return false;
	}

	public void initialize(Path path) {
		try {
			this.doc = PDDocument.load(path.toFile());
			this.fullText = getText(this.doc);
		} catch (Exception ex) {

		}
	}

	public static boolean isPublish(String[] words, int i) {
		if (i == words.length - 1) {
			// Assuming Legit research papers as input which has over 1000 words
			// Not checking for fringe cases like a pdf with 2 words.
			// Special check for Last element cuz words[i+1] throws error

			// "Published on July 2017"
			// i-3 i-2 i-1 i
			for (yearSearchTerms search : yearSearchTerms.values()) {
				if (words[i - 1].equalsIgnoreCase(search.toString()) || words[i - 2].equalsIgnoreCase(search.toString())
						|| words[i - 3].equalsIgnoreCase(search.toString())
						|| words[i - 4].equalsIgnoreCase(search.toString())

				)
					return true;
			}
		} else if (i > 3) {
			// Same check as above except for the last clause (i+1)
			for (yearSearchTerms search : yearSearchTerms.values()) {
				if (words[i - 1].equalsIgnoreCase(search.toString()) || words[i - 2].equalsIgnoreCase(search.toString())
						|| words[i - 3].equalsIgnoreCase(search.toString())
						|| words[i - 4].equalsIgnoreCase(search.toString()))
					return true;
			}

			// "2016 IEE" "2016 SPRINGER"
			for (publisherList pub : publisherList.values()) {
				if (words[i + 1].equalsIgnoreCase(pub.toString()))
					return true;
			}
		} else {

			// "2016 IEE" "2014 SPRINGER" as 1st line in pdf.
			for (yearSearchTerms search : yearSearchTerms.values())
				if (words[i + 1].equalsIgnoreCase(search.toString()))
					return true;
		}

		return false;
	}

	public static boolean isNumeric(String str) {
		NumberFormat formatter = NumberFormat.getInstance();
		ParsePosition pos = new ParsePosition(0);
		formatter.parse(str, pos);
		if (str.length() == pos.getIndex()) {
			// System.out.println("Here : "+str.length());
			if (str.length() != 0) {
				Double yearCheck = Double.parseDouble(str);
				if (yearCheck >= 1500 && yearCheck <= 2017)
					return true; // Valid publish year
				else
					return false; // Number but not valid publish year;
			} else
				return false; // for str.length==0 . Dunno why this
								// is being passed.
		} else
			return false; // not numeric.
	}

	public static File getFiles(String Path) {
		// TBD : Gather all files in directory and return.
		// Using hardcoded path for now.
		return new File(Path);
	}

	public String getDoi() {
		// Splitting with spaces so something like "abc.123" is also a word
		String[] words = this.fullText.split("\\s+");
		for (int i = 0; i < words.length; i++) {
			// If "DOI:" or "doi:" is part of the word :
			if (words[i].matches("DOI:\\S+"))
				return words[i].replace("DOI:", "");
			if (words[i].matches("doi:\\S+"))
				return words[i].replace("doi:", "");

			if (words[i].equalsIgnoreCase("DOI") || words[i].equalsIgnoreCase("DOI:")) {
				// Checking if words[i+1] has " : " or " - " or something like
				// that
				// Eg: DOI : abc or DOI - abc
				if (words[i + 1].matches("\\W+"))
					return words[i + 2];
				else
					return words[i + 1];
			}
		}
		return "N/A";

	}

	public static String getText(PDDocument doc) {
		PDFTextStripper pdfStripper;
		try {
			pdfStripper = new PDFTextStripper();

			pdfStripper.setSortByPosition(true);
			// pdfStripper.setStartPage(1);
			// pdfStripper.setEndPage(1);
			return new String(pdfStripper.getText(doc));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getYears() {
		List<String> years = new ArrayList<String>();
		String[] words = this.fullText.split("\\W+");
		for (int i = 0; i < words.length; i++) {
			if (isNumeric(words[i]) && words[i].length() == 4) {
				if (isMonthNear(words, i) && isPublish(words, i))
					years.add(words[i - 1] + " " + words[i]);
				else if (isPublish(words, i))
					years.add(words[i]);
			}
		}

		// Returning 1st value at the moment.
		if (years.isEmpty())
			return "N/A";
		else
			return years.get(0);
	}

	public static List<String> getHeaderFooter(PDDocument doc, String regionName, Rectangle2D region) {
		try {
			PDFTextStripperByArea stripper;
			stripper = new PDFTextStripperByArea();
			stripper.setSortByPosition(true);
			stripper.addRegion(regionName, region);
			int numberOfPages = doc.getNumberOfPages();
			List<String> tempList = new ArrayList<String>();

			for (int i = 0; i < numberOfPages; i++) {
				String words[] = null;
				PDPage page = doc.getDocumentCatalog().getPages().get(i);
				page.getResources().getFontNames();
				stripper.extractRegions(page);
				String text = stripper.getTextForRegion(regionName);
				words = text.split("\\W+");
				tempList.addAll(Arrays.asList(words));
			}
			tempList.removeIf(str -> isNumeric(str));
			return tempList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getPublisher() {
		try {
			List<String> headerText = new ArrayList<String>();
			List<String> footerText = new ArrayList<String>();
			String headerRegion = "header";
			String footerRegion = "footer";

			// Calculate approx header and footer positions
			PDPage pages = this.doc.getDocumentCatalog().getPages().get(0);
			float width = pages.getMediaBox().getWidth();
			float height = pages.getMediaBox().getHeight();

			Rectangle2D headerRectangle = new Rectangle2D.Double(0, 0, width, height / 4);
			Rectangle2D footerRectangle = new Rectangle2D.Double(0, height - height / 4, width, height / 4);

			headerText = getHeaderFooter(this.doc, headerRegion, headerRectangle);
			footerText = getHeaderFooter(this.doc, footerRegion, footerRectangle);

			for (int i = 0; i < headerText.size(); i++) {
				for (publisherList pub : publisherList.values())
					if (pub.toString().equalsIgnoreCase(headerText.get(i)))
						return pub.toString();

			}
			for (int i = 0; i < footerText.size(); i++) {
				for (publisherList pub : publisherList.values())
					if (pub.toString().equalsIgnoreCase(footerText.get(i)))
						return pub.toString();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "N/A";
	}

	/**
	 * 
	 * @param args
	 * 
	 *            Only for Test purpose
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		PublisherYear publisherYear = new PublisherYear();
		try {
			// Logger.getRootLogger().setLevel(Level.OFF);
			String filePath = args[0];
			// ContentExtractor extr = new ContentExtractor();
			// String filePath = "/Users/ass6ash/Downloads/JonathanPdf/Document
			// 5.pdf";
			// InputStream input = new FileInputStream(filePath);
			// extr.setPDF(input);
			// String pub = extr.getMetadata().getTitle();
			// System.out.println(pub);
			File file = getFiles(filePath);
			String year, doi, publisher;
			publisherYear.initialize(file.toPath());
			PDDocument doc = PDDocument.load(file);
			// publisherYear.getText(doc);
			// System.out.println(text);
			doi = publisherYear.getDoi();
			year = publisherYear.getYears();
			publisher = publisherYear.getPublisher();
			System.out.println("year = " + year);
			System.out.println("doi = " + doi);
			// System.out.println("Publisher = " + publisher);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
