package edu.wright.cs;

import java.io.IOException;
import java.util.HashMap;

import org.slf4j.LoggerFactory;

import edu.wright.cs.cermine.CerminePdf;
import edu.wright.cs.itext.ITextPdf;
import edu.wright.cs.util.Constants;
import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.cermine.metadata.model.DocumentMetadata;
import pl.edu.icm.cermine.tools.timeout.TimeoutException;

public class pdfRenamer {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(pdfRenamer.class);
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

	// private static ArrayList<String> fileNames;
	private static HashMap<String, String> nameToDir;
	private static ITextPdf iTextPdf;
	private static CerminePdf cerminePdf;
	private static DocumentMetadata metadata;

	private static void printHelp() {
		System.out.println("Commands: java -jar PageRangeLocation.jar PdfFile");
	}

	private static void extractPageRange() {
		logger.debug("extractPageRange() starts");
		String fP = metadata.getFirstPage();
		String lP = metadata.getLastPage();
		if (fP != null && lP != null) {
			int firstPage = Integer.valueOf(metadata.getFirstPage());
			int lastPage = Integer.valueOf(metadata.getLastPage());
			logger.debug("first: " + firstPage + " last: " + lastPage + "  total: " + iTextPdf.getTotalPage());
			if (lastPage - firstPage == iTextPdf.getTotalPage())
				System.out.println("Page Range: " + firstPage + "-" + lastPage);
			else {
				System.out.println("Page Range: not found.");
			}
		}
		logger.debug("extractPageRange() ends");

	}

	private static void extractLocation() {
		logger.debug("extractLocation() starts");
		iTextPdf.selectProbaleTextForLocation();

		iTextPdf.verifyProbableLocation();
		String location = iTextPdf.extractLocationFromProbableLines();
		if (location.length() > 1)
			System.out.println("Location: " + location);
		else {
			System.out.println("Location: not found.");
		}
		logger.debug("extractLocation() ends");
	}

	private static void startProcessing(String path) throws AnalysisException, IOException, TimeoutException {
		logger.debug("startProcessing() starts");
		cerminePdf = new CerminePdf(path);
		metadata = cerminePdf.getMetadata();

		iTextPdf = new ITextPdf(path, metadata.getAuthors());
		iTextPdf.extractLocationFromProbableLines();

		extractPageRange();
		extractLocation();
		logger.debug("startProcessing() ends");
	}

	public static void main(String[] args) {
		try {
			startProcessing(Constants.testPdfName);
		} catch (Exception e) {

		}
		// try {
		// appRunDir = System.getProperty("user.dir");
		// if (args.length == 0) {
		// System.out.println(fileNotGiven);
		// printHelp();
		// } else if (args.length == 1) {
		// if (args[0].endsWith(".pdf")) {
		// Path path = java.nio.file.Paths.get(args[0]).toAbsolutePath();
		// startProcessing(path);
		// } else {
		// System.out.println(fileNotPdf);
		// }
		// }
		//
		// } catch (Exception ex) {
		//
		// }
	}

}
