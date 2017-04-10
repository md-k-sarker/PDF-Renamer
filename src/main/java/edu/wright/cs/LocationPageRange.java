package edu.wright.cs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.LoggerFactory;

import edu.wright.cs.cermine.CerminePdf;
import edu.wright.cs.itext.ITextPdf;
import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.cermine.metadata.model.DocumentMetadata;
import pl.edu.icm.cermine.tools.timeout.TimeoutException;

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
	private static CerminePdf cerminePdf;
	private static DocumentMetadata metadata;

	public LocationPageRange() {

	}

	public void initialize(Path path) {
		try {

			cerminePdf = new CerminePdf(path.toString());
			metadata = cerminePdf.getMetadata();

			iTextPdf = new ITextPdf(path.toString(), metadata.getAuthors());

		} catch (AnalysisException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
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
		//logger.debug("extractPageRange() ends");

	}

	protected String extractLocation() {
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
		}
		else {
			System.out.println("Location: not found.");
			return "N/A";
		}
		//logger.debug("extractLocation() ends");
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
