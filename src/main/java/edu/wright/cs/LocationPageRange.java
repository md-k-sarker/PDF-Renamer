package edu.wright.cs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.LoggerFactory;

import edu.wright.cs.cermine.CerminePdf;
import edu.wright.cs.itext.ITextPdf;
import edu.wright.cs.util.Constants;
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
	 * @return the cerminePdf
	 */
	public static CerminePdf getCerminePdf() {
		return cerminePdf;
	}

	/**
	 * @param cerminePdf
	 *            the cerminePdf to set
	 */
	public static void setCerminePdf(CerminePdf cerminePdf) {
		LocationPageRange.cerminePdf = cerminePdf;
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

	private static CerminePdf cerminePdf;
	private static DocumentMetadata metadata;

	public LocationPageRange() {

	}

	/**
	 * Initialize iTextPdf object and Cermine pdf Object.
	 * 
	 * @param path
	 * @throws IOException 
	 * @throws AnalysisException 
	 * @pre path is not null
	 * @post cerminePdf, iTextPdf is not null
	 */
	public void initialize(Path path) throws AnalysisException, IOException {

		assert path != null;
		cerminePdf = new CerminePdf(path.toString());
		metadata = cerminePdf.getMetadata();

		iTextPdf = new ITextPdf(path.toString(), metadata.getAuthors());

		assert cerminePdf != null && iTextPdf != null;

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
	 * Extract location from the pdf
	 * 
	 * @return location of the pdf
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
			System.out.println("Location: " + location);
			return location;
		} else {
			System.out.println("Location: not found.");
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

}
