package edu.wright.cs.itext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.slf4j.LoggerFactory;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.FilteredTextRenderListener;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.RegionTextRenderFilter;
import com.itextpdf.text.pdf.parser.RenderFilter;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import edu.wright.cs.util.Constants;
import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.cermine.metadata.model.DocumentAuthor;
import pl.edu.icm.cermine.tools.timeout.TimeoutException;

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
		logger.debug("pdfFileName: " + pdfFileName);
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

	public int getTotalPage() {
		if (pdfReader != null) {
			return pdfReader.getNumberOfPages();
		}
		return 0;
	}

	public void selectProbaleTextForLocation() {
		probableLocationLines = new HashMap<Integer, String>();

		if (this.lines != null && !this.lines.isEmpty()) {

			for (int lineNo : this.lines.keySet()) {
				for (Locale country : locale) {
					if (this.lines.get(lineNo).contains(country.getDisplayCountry())) {
						probableLocationLines.put(lineNo, lines.get(lineNo));
						logger.debug("lineNo: " + lineNo + " text: " + lines.get(lineNo));
					}
				}
			}
		}

	}

	public String extractLocationFromProbableLines() {
		String location = "";
		String city = "";
		boolean shouldStop = false;
		if (this.probableLocationLines != null && !this.probableLocationLines.isEmpty()) {
			for (int lineNo : this.probableLocationLines.keySet()) {

				String[] split = this.probableLocationLines.get(lineNo).split("[,.]+");
				for (Locale country : locale) {
					for (int i = 0; i < split.length; i++) {

						//logger.debug("split[i]: " + split[i] + "\t country: " + country.getDisplayCountry());

						if (split[i].trim().equals(country.getDisplayCountry())
								|| split[i].trim().equals(country.getDisplayCountry() + ".")) {
							shouldStop = true;

							if (i - 1 >= 0) {
								city = split[i - 1];
							}
							if (city.length() > 1) {
								location = city + ", " + split[i];
							} else {
								location = split[i];
							}
							logger.debug("city: " + city + "\tlocation: " + location);
							return location;
							// break;
						} else {
							// logger.debug("split[i].notequals(country.getDisplayCountry()):
							// " + split[i]);
						}
					}
				}

				if (shouldStop)
					break;
			}
			return location;
		}
		return location;

	}

	public HashMap<Integer, String> getProbableLocation() {
		return this.probableLocationLines;
	}

	/*
	 * to-do Need to retest agian
	 */
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
						// probableLocationLines.remove(i);
					} catch (Exception ex) {

					}
				}

			}
		}

	}

	/*
	 * Only for test
	 */
	public static void main(String[] args) throws AnalysisException, IOException, TimeoutException {
		// ITextPdf pdf;
		// CerminePdf cPdf;
		// cPdf = new CerminePdf(Constants.testPdfName);
		// pdf = new ITextPdf(Constants.testPdfName,
		// cPdf.getMetadata().getAuthors());
		// pdf.selectProbaleTextForLocation();
		//
		// pdf.verifyProbableLocation();
		// String location = pdf.extractLocationFromProbableLines();
		// logger.debug("Location: " + location);
		// for (int i : pdf.getProbableLocation().keySet()) {
		// logger.debug("Line: " + i + " \tText: " +
		// pdf.getProbableLocation().get(i));
		// }

		// try (Stream<Path> paths =
		// Files.walk(Paths.get(Constants.testFileDir))) {
		// paths.forEach(filePath -> {
		// if (Files.isRegularFile(filePath)) {
		//
		// if (filePath.toString().endsWith(".pdf")) {
		//
		// try {
		// cPdf = new CerminePdf(filePath.toString());
		// pdf = new ITextPdf(filePath.toString(),
		// cPdf.getMetadata().getAuthors());
		// pdf.selectProbaleTextForLocation();
		//
		// pdf.verifyProbableLocation();
		//
		// for (int i : pdf.getProbableLocation().keySet()) {
		// logger.debug("Line: " + i + " \tText: " +
		// pdf.getProbableLocation().get(i));
		// }
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// // e.printStackTrace();
		// }
		//
		// } else {
		// System.out.println(filePath.getFileName().toString() + " not a pdf
		// file.");
		// }
		// }
		//
		// });
		// } catch (Exception ex) {
		//
		// }
	}

}
