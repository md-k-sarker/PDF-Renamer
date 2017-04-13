package edu.wright.cs;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.LoggerFactory;

import edu.wright.cs.itext.ITextPdf;
import edu.wright.cs.util.Constants;
import pl.edu.icm.cermine.ContentExtractor;
import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.cermine.metadata.model.DocumentMetadata;
import pl.edu.icm.cermine.tools.timeout.TimeoutException;

/**
 * 
 * @author sarker
 *
 */

public class LocationPageRange {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(LocationPageRange.class);

	private static boolean isDirectory = false;
	private static String flagAndFileNotGiven = "Flag and Input file/directory is not specified.";
	private static String fileNotGiven = "Input file is not specified.";
	private static String dirNotGiven = "Input directory is not specified.";
	private static String multipleDirGiven = "Multiple directory is not permitted.";
	private static String flagNotGiven = "Input flag is not specified.";
	private static String appRunDir = "";
	private static String fileDir = "";
	private static String dirNotContainPdf = "Directory does not have any pdf file.";
	private static String fileNotPdf = "File is not pdf";
	private static boolean batchMode = true;

	private static ITextPdf iTextPdf;
	private static PageRange pageRange;
	private static DocumentMetadata metadata;

	/**
	 * @return the iTextPdf
	 */
	public static ITextPdf getiTextPdf() {
		return iTextPdf;
	}

	/**
	 * @param iTextPdf
	 *            the iTextPdf to set
	 */
	public static void setiTextPdf(ITextPdf iTextPdf) {
		LocationPageRange.iTextPdf = iTextPdf;
	}

	/**
	 * @return the pageRange
	 */
	public static PageRange getCerminePdf() {
		return pageRange;
	}

	/**
	 * @param pageRange
	 *            the pageRange to set
	 */
	public static void setCerminePdf(PageRange pageRange) {
		LocationPageRange.pageRange = pageRange;
	}

	/**
	 * @return the metadata
	 */
	public static DocumentMetadata getMetadata() {
		return metadata;
	}

	/**
	 * @param metadata
	 *            the metadata to set
	 */
	public static void setMetadata(DocumentMetadata metadata) {
		LocationPageRange.metadata = metadata;
	}

	public LocationPageRange() {

	}

	/**
	 * Initialize iTextPdf object and Cermine pdf Object.
	 * 
	 * @param path
	 * @throws IOException
	 * @throws AnalysisException
	 * @pre path is not null
	 * @post pageRange, iTextPdf is not null
	 */
	public void initialize(Path path) throws AnalysisException, IOException {

		assert path != null;
		pageRange = new PageRange(path.toString());
		metadata = pageRange.getMetadata();

		iTextPdf = new ITextPdf(path.toString(), metadata.getAuthors());

		assert pageRange != null && iTextPdf != null;

	}

	/**
	 * Extract Page Range if possible. If not possible then return N/A
	 * 
	 * @return PageRange of the pdf.
	 */
	protected String extractPageRange() {
		logger.debug("extractPageRange() starts");
		String fP = metadata.getFirstPage();
		String lP = metadata.getLastPage();
		logger.debug("first: " + fP + " last: " + lP);
		if (fP != null && lP != null) {
			System.out.println("Page Range: " + fP + "-" + lP);
			return (fP + "-" + lP);

		} else {
			System.out.println("Page Range: not found.");
			return ("N/A");
		}
		// logger.debug("extractPageRange() ends");
	}

	/**
	 * Extract iTextPdf from the pdf
	 * 
	 * @return iTextPdf of the pdf
	 * @pre iTextPdf is not null
	 */
	protected String extractLocation() {
		assert iTextPdf != null;
		logger.debug("extractLocation() starts");
		iTextPdf.selectProbaleTextForLocation();
		/*
		 * to-do need to fix it
		 */
		// iTextPdf.verifyProbableLocation();

		String location = iTextPdf.extractLocationFromProbableLines();
		if (location.length() > 1) {
			System.out.println("ITextPdf: " + location);
			return location;
		} else {
			System.out.println("ITextPdf: not found.");
			return "N/A";
		}
		// logger.debug("extractLocation() ends");
	}

	public static void main(String[] args) {

		for (int i = 0; i < args.length; i++) {
			logger.debug("command line args[" + i + "]\t" + args[i]);
		}

		try {
			if (batchMode) {
				// runInBatchMode(args);
			} else {
				// runInSingleMode(args);
			}
		} catch (Exception ex) {
		}
	}

	private static class PageRange {

		private static org.slf4j.Logger logger = LoggerFactory.getLogger(PageRange.class);

		Path file = Paths.get(Constants.outputFileDir + "result.txt");

		private ContentExtractor extractor;
		private InputStream inputStream;
		private DocumentMetadata metadata;

		public PageRange(String fileName) throws AnalysisException, IOException {

			this.extractor = new ContentExtractor();
			this.inputStream = new FileInputStream(fileName);
			this.extractor.setPDF(inputStream);
			this.metadata = this.extractor.getMetadata();

		}

		public DocumentMetadata getMetadata() throws TimeoutException, AnalysisException {
			return this.metadata;
		}

		public String getPageRange() {
			String firstPage = this.metadata.getFirstPage();
			String lastPage = this.metadata.getLastPage();
			if (firstPage == null || lastPage == null)
				return "Page Range not found.";
			return firstPage + "-" + lastPage;
		}

		public static void main(String[] args) {

			try {
				PageRange cPdf = new PageRange(Constants.testPdfName);
			} catch (Exception e) {

			}
		}
	}

}
