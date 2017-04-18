package edu.wright.cs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.FilteredTextRenderListener;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.RegionTextRenderFilter;
import com.itextpdf.text.pdf.parser.RenderFilter;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

/**
 * 
 * @author Jayanth
 *
 */

public class AuthorIssue {

	private static final String COMMA_DELIMITER = ",";
	private static final String PERIOD_DELIMITER = ".";
	private static final String WHITESPACE_DELIMITER = "#############";
	private static final String ABSTRACT = "Abstract";
	private static final String NOT_APPLICABLE = "N/A";
	private static final String NEW_LINE = "\n";
	private static final int FIRST_LINE = 1;
	private static final int START_LINE = 3;
	private static final int END_LINE = 7;
	private static final int PAGE_NUMBER = 1;
	private static Pattern pattern = Pattern.compile("([0-9])");
	private Matcher matcher = null;

	private static final String[] SANITIZE_DICTIONARY = { "member", "institute", "technology", "university",
			"communications", "ieee", "acm", "college", "information", "fellow", "department", "computer", "science",
			"@", "email", "school", "management", "organization" };

	/**
	 * Method to extract Author Names from a pdf file
	 * 
	 * @param pdf
	 * @throws IOException
	 */
	public String extractAuthors(File pdf) throws IOException {
		assert pdf != null && pdf.isFile();
		String line = null;
		String author = "";
		StringBuilder builder = new StringBuilder();
		Map<Integer, String> pdfLines = readPdfLines(pdf, true);
		for (int i = START_LINE; i <= END_LINE; i++) {
			line = pdfLines.get(i).trim();
			assert line != null;
			if (line.toLowerCase().startsWith(ABSTRACT.toLowerCase())) {
				break;
			} else if (!line.isEmpty() && checkIfAuthorNameExists(line)) {
				author = extractAuthorFromLine(line);
				if (!author.trim().isEmpty()) {
					builder.append(author);
					builder.append(COMMA_DELIMITER);
				}
			}
		}
		if (builder.length() > 0) {
			author = builder.deleteCharAt(builder.length() - 1).toString();
			if (author.trim().isEmpty()) {
				author = NOT_APPLICABLE;
			}
		} else {
			author = NOT_APPLICABLE;
		}
		System.out.println("AUTHORS := " + author);
		assert author != null && author != "";
		return author;
	}

	/**
	 * Method extracts comma separated author names from given line
	 * 
	 * @param line
	 * @return
	 */
	private String extractAuthorFromLine(String line) {
		String author = "";
		StringBuilder builder = new StringBuilder();
		String token = null;
		StringTokenizer tokenizer = new StringTokenizer(line, COMMA_DELIMITER);

		while (tokenizer.hasMoreTokens()) {
			token = tokenizer.nextToken();
			token = sanitizeConjunction(token);
			if (!sanitizeForName(token)) {
				builder.append(token);
				builder.append(COMMA_DELIMITER);
			}
		}
		if (builder.length() > 0) {
			author = builder.deleteCharAt(builder.length() - 1).toString();
		}
		return author.trim();
	}

	/**
	 * Method to remove conjunctions as of now only "and"
	 * 
	 * @param str
	 * @return
	 */
	private String sanitizeConjunction(String str) {
		String sanitizedStr=str;
		if (sanitizedStr.contains("and")) {
			sanitizedStr = sanitizedStr.replace("and", "").trim();
		}
		return sanitizedStr;
	}

	/**
	 * Method to check if Author Name exists in a give line
	 * 
	 * @param line
	 * @return
	 */
	private boolean checkIfAuthorNameExists(String line) {
		return line.split(COMMA_DELIMITER).length >= 2;
	}

	/**
	 * @param chkStr
	 * @return
	 */
	private boolean sanitizeForName(String chkStr) {
		boolean result = false;
		if(null!= chkStr){
			for (String str : SANITIZE_DICTIONARY) {
				matcher = pattern.matcher(chkStr);
				if (chkStr.toLowerCase().contains(str) || matcher.find()) {
					result = true;
					break;
				}
			}
		}
		
		return result;
	}

	/**
	 * Method extracts issue number from given pdf
	 * 
	 * @param pdf
	 */
	public String extractIssueNo(File pdf) {
		assert pdf != null && pdf.isFile();
		Map<Integer, String> pdfLines = readPdfLines(pdf, false);
		String infoLine = pdfLines.get(FIRST_LINE);
		String issueNo = extractIssueNoFromString(infoLine.trim());
		if (null == issueNo) { // if first line/header is not present, look for
								// last line/footer
			infoLine = pdfLines.get(pdfLines.size());
			issueNo = extractIssueNoFromString(infoLine.trim());
		}
		System.out.println("ISSUE NO := " + issueNo);
		assert issueNo != null && issueNo != "";
		return issueNo;
	}

	/**
	 * 
	 * Method extracts issue number from given line
	 * 
	 * @param infoLine
	 * @return
	 */
	private String extractIssueNoFromString(String infoLine) {
		String issueNo = NOT_APPLICABLE;
		String[] splitStr = infoLine.split(COMMA_DELIMITER);
		if (splitStr.length >= 4) {
			issueNo = splitStr[2];
			issueNo = issueNo.substring(issueNo.indexOf(PERIOD_DELIMITER) + 1).trim();
		}
		return issueNo;
	}

	/**
	 * Method reads the first page of PDF line by line
	 * 
	 * @param pdfFile
	 * @param withNewLines
	 * @return
	 */
	private Map<Integer, String> readPdfLines(File pdfFile, boolean withNewLines) {
		Map<Integer, String> lines = new HashMap<Integer, String>();
		TextExtractionStrategy strategy;
		String firstPageText;
		PdfReader pdfReader = null;
		try {
			pdfReader = new PdfReader(pdfFile.getAbsolutePath());
			Rectangle rectangle = pdfReader.getPageSize(PAGE_NUMBER);
			RenderFilter filter = new RegionTextRenderFilter(rectangle);
			strategy = new FilteredTextRenderListener(new LocationTextExtractionStrategy(), filter);
			firstPageText = PdfTextExtractor.getTextFromPage(pdfReader, 1, strategy);
			int lineNo = 1;
			int strLen = 0;
			for (String s : firstPageText.split(NEW_LINE)) {
				strLen = s.trim().length();
				if (withNewLines) {
					if (strLen == 0) {
						lines.put(lineNo, WHITESPACE_DELIMITER);
					} else {
						lines.put(lineNo, s);
						lineNo++;
					}
				} else {
					if (strLen > 0) {
						lines.put(lineNo, s);
						lineNo++;
					}
				}
			}
		} catch (IOException e) {
			System.out.println("IOException for " + pdfFile);
			e.printStackTrace();
		} finally {
			if (null != pdfReader) {
				pdfReader.close();
			}
		}
		return lines;
	}

	/**
	 * Method to list all the files in the given folder
	 * 
	 * @param fileName
	 * @return
	 */
	private List<File> getFiles(String fileName) {
		List<File> pdfList = null;
		try {
			pdfList = Files.walk(Paths.get(fileName)).filter(Files::isRegularFile).map(Path::toFile)
					.collect(Collectors.toList());
		} catch (IOException e) {
			System.out.println("IOException for " + fileName);
			e.printStackTrace();
		}
		return pdfList;
	}

	/**
	 * 
	 * @param args
	 * @throws IOException
	 * 
	 *             Only for Test
	 */
	public static void main(String[] args) throws IOException {
		AuthorIssue pdfRenamer = new AuthorIssue();
		if (args.length < 1) {
			System.out.println("PLEASE PROVIDE PDF FOLDER PATH");
			System.exit(1);
		}
		String pdfPath = args[0];
		List<File> pdfs = pdfRenamer.getFiles(pdfPath);
		for (File pdf : pdfs) {
			System.out.println("EXTRACTING DETAILS FOR " + pdf.getName());
			pdfRenamer.extractAuthors(pdf);
			pdfRenamer.extractIssueNo(pdf);

			System.out.println("\n \n");
		}
	}

}
