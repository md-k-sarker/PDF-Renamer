package edu.wright.cs;

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

public class Location {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(Location.class);
	private Rectangle rectangle;
	private PdfReader pdfReader;
	private String firstPageText;
	private int pageNumber = 1;
	private ArrayList<Locale> locale;
	private HashMap<Integer, String> lines;
	int[] searchLines = { -1, 0, 1 };
	List<DocumentAuthor> authorNames;

	/**
	 * @return the authorNames
	 */
	public List<DocumentAuthor> getAuthorNames() {
		return authorNames;
	}

	/**
	 * @param authorNames the authorNames to set
	 */
	public void setAuthorNames(List<DocumentAuthor> authorNames) {
		this.authorNames = authorNames;
	}

	/**
	 * @return the lines
	 */
	public HashMap<Integer, String> getLines() {
		return lines;
	}

	/**
	 * @param lines
	 *            the lines to set
	 */
	public void setLines(HashMap<Integer, String> lines) {
		this.lines = lines;
	}

	private HashMap<Integer, String> probableLocationLines;

	/**
	 * @return the probableLocationLines
	 */
	public HashMap<Integer, String> getProbableLocationLines() {
		return probableLocationLines;
	}

	/**
	 * @param probableLocationLines
	 *            the probableLocationLines to set
	 */
	public void setProbableLocationLines(HashMap<Integer, String> probableLocationLines) {
		this.probableLocationLines = probableLocationLines;
	}

	/**
	 * 
	 * @return
	 */
	public int getTotalPage() {
		if (pdfReader != null) {
			return pdfReader.getNumberOfPages();
		}
		return 0;
	}


	/**
	 * 
	 * @param pdfFileName
	 * @param authorNames
	 * @throws IOException
	 */
	public Location(String pdfFileName) throws IOException {
		lines = new HashMap<Integer, String>();

		pdfReader = new PdfReader(pdfFileName);
		logger.debug("pdfFileName: " + pdfFileName);

	}

	/**
	 * Extracts line of texts from first page of pdf.
	 * 
	 * @throws IOException
	 * @pre pdfReader is not null;
	 */
	public void selectFirstPageTexts() throws IOException {

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

	/**
	 * Select probable locations from the text.
	 * 
	 * @pre line != null
	 */
	public void selectProbaleTextForLocation() {
		assert this.lines != null;
		logger.debug("selectProbaleTextForLocation started");
		probableLocationLines = new HashMap<Integer, String>();
		locale = Constants.getCountryNames();

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
		logger.debug("selectProbaleTextForLocation finished");

	}

	/**
	 * Extract location from the probable lines.
	 * 
	 * @return location if possible otherwise return empty location.
	 * @pre probableLocationLines is not null
	 */
	public String extractLocationFromProbableLines() {

		logger.debug("extractLocationFromProbableLines started");

		String location = "";
		String city = "";
		boolean shouldStop = false;
		if (this.probableLocationLines != null && !this.probableLocationLines.isEmpty()) {
			for (int lineNo : this.probableLocationLines.keySet()) {

				String[] split = this.probableLocationLines.get(lineNo).split("[,.]+");
				for (Locale country : locale) {
					for (int i = 0; i < split.length; i++) {

						// logger.debug("split[i]: " + split[i] + "\t country: "
						// + country.getDisplayCountry());

						if (split[i].trim().equals(country.getDisplayCountry())
								|| split[i].trim().equals(country.getDisplayCountry() + ".")) {
							shouldStop = true;

							if (i - 1 >= 0) {
								city = split[i - 1];
								// trimmed ufter unit test
								city = city.trim();
							}
							if (city.length() > 1) {
								// trimmed ufter unit test
								location = city.trim() + ", " + split[i].trim();
							} else {
								location = split[i];
							}
							logger.debug("city: " + city + "\tlocation: " + location);
							logger.debug("extractLocationFromProbableLines finished");
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
			logger.debug("extractLocationFromProbableLines finished");
			return location;
		}
		logger.debug("extractLocationFromProbableLines finished");
		return location;
	}

	public HashMap<Integer, String> getProbableLocation() {
		return this.probableLocationLines;
	}



	/*
	 * Only for test
	 */
	public static void main(String[] args) throws AnalysisException, IOException, TimeoutException {
		// Location pdf;
		// PageRange cPdf;
		// cPdf = new PageRange(Constants.testPdfName);
		// pdf = new Location(Constants.testPdfName,
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
		// cPdf = new PageRange(filePath.toString());
		// pdf = new Location(filePath.toString(),
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
		// TODO Auto-generated catch block
		// e.printStackTrace();
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
