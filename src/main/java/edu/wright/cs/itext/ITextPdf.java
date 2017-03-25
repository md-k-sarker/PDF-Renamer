package edu.wright.cs.itext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.slf4j.LoggerFactory;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.FilteredTextRenderListener;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.RegionTextRenderFilter;
import com.itextpdf.text.pdf.parser.RenderFilter;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import edu.wright.cs.cermine.CerminePdf;
import edu.wright.cs.util.Constants;
import pl.edu.icm.cermine.metadata.model.DocumentAuthor;

public class ITextPdf {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(ITextPdf.class);
	private Rectangle rectangle;
	private PdfReader pdfReader;
	private Rectangle topRectangle;
	private Rectangle bottomRectangle;
	private String firstPageText;
	private int pageNumber = 1;
	private ArrayList<Locale> locale;
	private HashMap<Integer, String> lines;
	private HashMap<Integer, String> probableLocationLines;
	private String country;
	private String city;
	int[] searchLines = { -1, 0, 1 };
	List<DocumentAuthor> authorNames;

	public ITextPdf(String pdfFileName, List<DocumentAuthor> authorNames) throws IOException {
		lines = new HashMap<Integer, String>();
		this.authorNames = authorNames;
		locale = Constants.getCountryNames();
		pdfReader = new PdfReader(pdfFileName);
		rectangle = pdfReader.getPageSize(pageNumber);

		RenderFilter filter = new RegionTextRenderFilter(rectangle);

		TextExtractionStrategy strategy;
		strategy = new FilteredTextRenderListener(new LocationTextExtractionStrategy(), filter);
		firstPageText = PdfTextExtractor.getTextFromPage(pdfReader, pageNumber, strategy);
		int lineNo = 1;
		for (String s : firstPageText.split("\n")) {
			lines.put(lineNo, s);
			lineNo++;
		}
	}

	public void selectProbaleTextForLocation() {
		probableLocationLines = new HashMap<Integer, String>();

		if (this.lines != null && !this.lines.isEmpty()) {

			for (int lineNo : this.lines.keySet()) {
				for (Locale country : locale) {
					if (this.lines.get(lineNo).contains(country.getDisplayCountry())) {
						probableLocationLines.put(lineNo, lines.get(lineNo));
					}
				}
			}
		}

	}

	public HashMap<Integer, String> getProbableLocation() {
		return this.probableLocationLines;
	}

	@SuppressWarnings("finally")
	public void verifyProbableLocation() {

		boolean removed;
		ArrayList<Integer> shouldBeRemoved;

		if (this.probableLocationLines != null && this.lines != null && !this.probableLocationLines.isEmpty()) {
			for (int lineNo : this.probableLocationLines.keySet()) {
				// Search for top 1 upper line and lower 1 line whether it
				// has author name or email
				shouldBeRemoved = new ArrayList<>();
				for (int sline : searchLines) {
					int currentSLine = lineNo + sline;
					if (this.lines.containsKey(currentSLine)) {
						removed = false;
						for (DocumentAuthor author : authorNames) {
							if (author.getName() != null) {
								if (this.lines.get(currentSLine).contains(author.getName())) {

									removed = true;
									// this.probableLocationLines.remove(lineNo);
									shouldBeRemoved.add(lineNo);
									break;
								}
							}
							if (author.getEmail() != null) {
								if (this.lines.get(currentSLine).contains(author.getEmail())) {

									removed = true;
									// this.probableLocationLines.remove(lineNo);
									shouldBeRemoved.add(lineNo);
									break;
								}
							}
						}
						if (removed)
							break;

					}
				}
				for (int i : shouldBeRemoved) {
					try {
						probableLocationLines.remove(i);
					} catch (Exception ex) {

					}
				}

			}
		}

	}

	public String extractLocationFromProbableLines() {
		if (this.probableLocationLines != null && !this.probableLocationLines.isEmpty()) {
				
			for (Locale country : locale) {

			}
		}
		return "";
	}

	/*
	 * Only for test
	 */
	public static void main(String[] args) {

		try (Stream<Path> paths = Files.walk(Paths.get(Constants.testFileDir))) {
			paths.forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {

					if (filePath.toString().endsWith(".pdf")) {

						ITextPdf pdf;
						CerminePdf cPdf;
						try {
							cPdf = new CerminePdf(filePath.toString());
							pdf = new ITextPdf(filePath.toString(), cPdf.getMetadata().getAuthors());
							pdf.selectProbaleTextForLocation();

							pdf.verifyProbableLocation();

							for (int i : pdf.getProbableLocation().keySet()) {
								logger.debug("Line: " + i + " \tText: " + pdf.getProbableLocation().get(i));
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} else {
						System.out.println(filePath.getFileName().toString() + " not a pdf file.");
					}
				}

			});
		} catch (Exception ex) {

		}
	}

}
