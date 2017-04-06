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
	private static boolean batchMode = true;

	private static ITextPdf iTextPdf;
	private static CerminePdf cerminePdf;
	private static DocumentMetadata metadata;

	private static void printHelp() {
		if (batchMode) {
			System.out.println("\nCommands:  pdfRenamer -flag Name");
			System.out.println("\t   -flag: indicates file or directory.");
			System.out.println("\t	-f: indicates file ");
			System.out.println(
					"\t	-d: indicates directory. Will do batch operation to all files with .pdf extension. \n\t\t    Will not search recursively for inner directory.");
			System.out.println("\t    Name: Name of directory or file.");
			System.out.println("\t\t  Multiple file name seperated by space can be given.");
			System.out.println("\t\t  Multiple directory name is not permitted.");
		} else {
			System.out.println("\nCommands: java -jar PageRangeLocation.jar PdfFile");
		}
	}

	private static void extractPageRange() {
		logger.debug("extractPageRange() starts");
		String fP = metadata.getFirstPage();
		String lP = metadata.getLastPage();
		logger.debug("first: " + fP + " last: " + lP);
		if (fP != null && lP != null) {
			System.out.println("Page Range: " + fP + "-" + lP);
		} else {
			System.out.println("Page Range: not found.");
		}
		logger.debug("extractPageRange() ends");

	}

	private static void extractLocation() {
		logger.debug("extractLocation() starts");
		iTextPdf.selectProbaleTextForLocation();
		/*
		 * to-do need to fix it
		 */
		// iTextPdf.verifyProbableLocation();

		String location = iTextPdf.extractLocationFromProbableLines();
		if (location.length() > 1)
			System.out.println("Location: " + location);
		else {
			System.out.println("Location: not found.");
		}
		logger.debug("extractLocation() ends");
	}

	private static void startProcessing(Path path) throws AnalysisException, IOException, TimeoutException {

		logger.debug("startProcessing() starts to process " + path.toString());
		System.out.println("\n--------Processing "+path);
		cerminePdf = new CerminePdf(path.toString());
		metadata = cerminePdf.getMetadata();

		iTextPdf = new ITextPdf(path.toString(), metadata.getAuthors());

		extractPageRange();
		extractLocation();
		logger.debug("startProcessing() ends");

	}

	private static void runInSingleMode(String[] args) throws TimeoutException, AnalysisException, IOException {

		appRunDir = System.getProperty("user.dir");
		if (args.length == 0) {
			System.out.println(fileNotGiven);
			printHelp();
		} else if (args.length == 1) {
			if (args[0].endsWith(".pdf")) {
				Path path = java.nio.file.Paths.get(args[0]).toAbsolutePath();
				startProcessing(path);
			} else {
				System.out.println(fileNotPdf);
			}
		}
	}

	private static void runInBatchMode(String[] args) throws IOException, TimeoutException, AnalysisException {

		if (args.length == 0) {
			System.out.println(flagAndFileNotGiven);
			printHelp();
		} else {

			if (args[0].equals("-f") || args[0].equals("-F")) {
				isDirectory = false;

				if (args.length == 1) {
					System.out.println(fileNotGiven);
					printHelp();
				} else if (args.length > 1) {

					for (int i = 1; i < args.length; i++) {
						if (args[i].endsWith(".pdf")) {
							Path path = java.nio.file.Paths.get(args[i]).toAbsolutePath();
							startProcessing(path);
						} else {
							System.out.println(fileNotPdf);
						}
					}
				}

			} else if (args[0].equals("-d") || args[0].equals("-D")) {
				isDirectory = true;

				if (args.length == 1) {
					System.out.println(dirNotGiven);
					printHelp();
				} else if (args.length == 2) {
					fileDir = args[1];
					fileDir = java.nio.file.Paths.get(fileDir).toAbsolutePath().toString();

					Files.walk(Paths.get(fileDir)).filter(f -> f.toString().endsWith(".pdf")).forEach(f -> {
						try {
							logger.debug("#####Processing file: " + f + "  ######");
							startProcessing(f);
						} catch (TimeoutException e) {
							e.printStackTrace();
						} catch (AnalysisException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					});

				} else if (args.length > 2) {
					System.out.println(multipleDirGiven);
					printHelp();
				}

			} else {
				System.out.println(flagNotGiven);
				printHelp();
			}

		}
	}

	public static void main(String[] args) {

		for (int i = 0; i < args.length; i++) {
			logger.debug("command line args[" + i + "]\t" + args[i]);
		}

		try {
			if (batchMode) {
				runInBatchMode(args);
			} else {
				runInSingleMode(args);
			}
		} catch (Exception ex) {
		}
	}

}
