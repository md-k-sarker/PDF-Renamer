package edu.wright.cs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.LoggerFactory;

import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.cermine.tools.timeout.TimeoutException;

/**
 * 
 * @author sarker
 *
 */

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
	private static String fileNotPdf = "File is not pdf";
	private static boolean batchMode = true;

	private static AuthorIssue authorIssue;
	private static LocationPageRange locationPageRange;
	private static PublisherYear publisherYear;
	private static Rename renamer;

	/**
	 * Method to print commands and helps
	 */
	private static void printHelp() {
		if (batchMode) {
			System.out.println("\nCommands:  pdfRenamer -flag renameFormat Name");
			System.out.println("\t   -flag: indicates file or directory.");
			System.out.println("\t	-f: indicates file ");
			System.out.println(
					"\t	-d: indicates directory. Will do batch operation to all files with .pdf extension. \n\t\t    Will not search recursively for inner directory.");
			System.out.println("\t renameFormat:\n" + "\t\t  %au - authors.\n" + "\t\t  %is - issueNo. \n"
					+ "\t\t  %lo - location.\n" + "\t\t  %pr - page range.\n" + "\t\t  %pu - publisher.\n"
					+ "\t\t  %yr - year.");
			System.out.println("\t\t  Enter format without spaces in between.");
			System.out.println("\t    Name: Name of directory or file.");
			System.out.println("\t\t  Multiple file name seperated by space can be given.");
			System.out.println("\t\t  Multiple directory name is not permitted.");
		} else {
			System.out.println("\nCommands: java -jar PageRangeLocation.jar PdfFile");
		}
	}


	/**
	 * Checks the arguments and start processing according to given arguments.
	 * call Startprocessing() for each pdf file.
	 * 
	 * @param Array
	 *            of Strings
	 * @throws IOException
	 * @throws TimeoutException
	 * @throws AnalysisException
	 * @pre: argument is not null
	 * @post:
	 */
	private static void processArguments(String[] args) throws IOException, TimeoutException, AnalysisException {
		assert args != null;
		if (args.length == 0) {
			System.out.println(flagAndFileNotGiven);
			printHelp();
		} else {

			if (args[0].equals("-f") || args[0].equals("-F")) {
				isDirectory = false;

				if (args.length <= 2) {
					System.out.println(fileNotGiven);
					printHelp();
				} else if (args.length > 2) {
					renamer.setFormat(args[1]);
					for (int i = 2; i < args.length; i++) {
						if (args[i].endsWith(".pdf")) {
							Path path = java.nio.file.Paths.get(args[i]).toAbsolutePath();
							startRenamingProcess(path);
						} else {
							System.out.println(fileNotPdf);
						}
					}
				}

			} else if (args[0].equals("-d") || args[0].equals("-D")) {
				isDirectory = true;

				if (args.length <= 2) {
					System.out.println(dirNotGiven);
					printHelp();
				} else if (args.length == 3) {
					fileDir = args[2];
					fileDir = java.nio.file.Paths.get(fileDir).toAbsolutePath().toString();
					renamer.setFormat(args[1]);
					Files.walk(Paths.get(fileDir)).filter(f -> f.toString().endsWith(".pdf")).forEach(f -> {
						try {
							logger.debug("#####Processing file: " + f + "  ######");
							startRenamingProcess(f);
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

	/**
	 * 
	 * @param Path
	 *            path
	 * @throws AnalysisException
	 * @throws IOException
	 * @throws TimeoutException
	 * @pre path is not null
	 * @pre authorIssue, locationPageRange, publisherYear, renamer is not null.
	 * @post
	 */
	private static void startRenamingProcess(Path path) throws AnalysisException, IOException, TimeoutException {

		assert path != null;
		assert authorIssue != null && locationPageRange != null && publisherYear != null && renamer != null;

		logger.debug("startProcessing() starts to process " + path.toString());
		System.out.println("\n--------Processing " + path);
		renamer.setPath(path);

		// extract location and pageRange
		locationPageRange.initialize(path);
		String pageRange = locationPageRange.extractPageRange();
		String location = locationPageRange.extractLocation();
		renamer.setPageRange(pageRange);
		renamer.setLocation(location);

		// extract Author and Issue
		String authors = authorIssue.extractAuthors(path.toFile());
		String issueNo = authorIssue.extractIssueNo(path.toFile());
		renamer.setAuthor(new StringBuilder(authors));
		renamer.setIssue(issueNo);

		// extract Publisher and year
		publisherYear.initialize(path);
		String publisher = publisherYear.getPublisher();
		String year = publisherYear.getYears();
		String doi = publisherYear.getDoi();
		renamer.setPublisher(publisher);
		renamer.setYear(year);
		renamer.setDoi(doi);
		System.out.println("DOI: " + doi);
		System.out.println("Publisher: " + publisher);
		System.out.println("Year: " + year);

		// Rename file
		renamer.renameFile();

		logger.debug("\n##############Processing() ends#############\n");

	}

	/**
	 * Will initialize all the constructors and class objects needed.
	 * 
	 * @post: authorIssue, locationPageRange, publisherYear, renamer is not
	 *        null.
	 */

	public static void init() {

		authorIssue = new AuthorIssue();
		locationPageRange = new LocationPageRange();
		publisherYear = new PublisherYear();
		renamer = new Rename();

		assert authorIssue != null && locationPageRange != null && publisherYear != null && renamer != null;
	}

	public static void main(String[] args) {

		for (int i = 0; i < args.length; i++) {
			logger.debug("command line args[" + i + "]\t" + args[i]);
		}

		try {
			init();
			processArguments(args);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
