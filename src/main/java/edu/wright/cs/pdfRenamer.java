package edu.wright.cs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

public class pdfRenamer {

	private static boolean isDirectory = false;
	private static String flagAndFileNotGiven = "Flag and Input file/directory is not specified.";
	private static String fileNotGiven = "Input file/directory is not specified.";
	private static String dirNotGiven = "Input directory is not specified.";
	private static String multipleDirGiven = "Multiple directory is not permitted.";
	private static String flagNotGiven = "Input flag is not specified.";
	private static String appRunDir = "";
	private static String fileDir = "";
	private static String dirNotContainPdf = "Directory does not have any pdf file.";

	// private static ArrayList<String> fileNames;
	private static HashMap<String, String> nameToDir;

	private static void printHelp() {
		System.out.println("Commands:  pdfRenamer -flag Name");
		System.out.println("\t-flag: indicates file or directory.");
		System.out.println("\t	-f: indicates file ");
		System.out.println(
				"\t	-d: indicates directory. Will do batch operation to all files with .pdf extension. \n\t\t    Will not search recursively for inner directory.");
		System.out.println("\tName: Name of directory or file.");
		System.out.println("\t\tMultiple file name seperated by space can be given.");
		System.out.println("\t\tMultiple directory name is not permitted.");
	}

	private static void extractPageRange(String fileName) {

	}

	private static void extractLocation(String fileName) {
//		CerminePdf cPdf = new CerminePdf(fileName);
//		cPdf.getMetadata();

	}

	private static void startProcessing() {
		for (String fileName : nameToDir.keySet()) {
			PdfText pdfText = new PdfText(new File(fileName));
			String text;
			try {
				text = pdfText.getFullText();
				System.out.println(text);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			extractPageRange(fileName);
			extractLocation(fileName);
		}
	}

	private static void storeFileNames(String[] args) throws IOException {
		if (isDirectory == true) {
			try (Stream<Path> paths = Files.walk(Paths.get(fileDir))) {
				paths.forEach(filePath -> {
					if (Files.isRegularFile(filePath)) {

						if (filePath.toString().endsWith(".pdf")) {
							nameToDir.put(filePath.getFileName().toString(), filePath.toString());
						} else {
							System.out.println(filePath.getFileName().toString() + " not a pdf file.");
						}
					}

				});
			}
		} else {
			for (int i = 1; i < args.length; i++) {
				if (args[i].endsWith(".pdf")) {
					Path path = java.nio.file.Paths.get(args[i]).toAbsolutePath();
					nameToDir.put(path.getFileName().toString(), path.toString());
				} else {
					System.out.println(args[i] + " not a pdf file.");
				}
			}
		}
	}

	private static void init() {
		// fileNames = new ArrayList<String>();
		nameToDir = new HashMap<String, String>();
	}

	public static void main(String[] args) throws IOException {

		try {

			appRunDir = System.getProperty("user.dir");

			if (args.length == 0) {
				System.out.println(flagAndFileNotGiven);
				printHelp();
			} else {
				init();
				if (args[0].equals("-f") || args.equals("-F")) {
					isDirectory = false;

					if (args.length == 1) {
						System.out.println(fileNotGiven);
						printHelp();
					} else {
						storeFileNames(args);

						if (!nameToDir.isEmpty()) {
							startProcessing();
						} else {
							System.out.println(fileNotGiven);
							printHelp();
						}
					}

				} else if (args[0].equals("-d") || args.equals("-D")) {
					isDirectory = true;
					if (args.length == 1) {
						System.out.println(dirNotGiven);
						printHelp();
					} else if (args.length == 2) {
						fileDir = args[1];
						fileDir = java.nio.file.Paths.get(fileDir).toAbsolutePath().toString();
						storeFileNames(args);

						if (!nameToDir.isEmpty()) {
							startProcessing();
						} else {
							System.out.println(dirNotContainPdf);
							printHelp();
						}
					} else if (args.length > 2) {
						System.out.println(multipleDirGiven);
						printHelp();
					}

				} else {
					System.out.println(flagNotGiven);
					printHelp();
				}

			}
		} catch (Exception ex) {

		}
	}

}
