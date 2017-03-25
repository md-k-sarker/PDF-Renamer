package edu.wright.cs.cermine;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import org.slf4j.LoggerFactory;

import edu.wright.cs.util.Constants;
import pl.edu.icm.cermine.ContentExtractor;
import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.cermine.metadata.model.DocumentMetadata;
import pl.edu.icm.cermine.structure.model.BxDocument;
import pl.edu.icm.cermine.structure.model.BxLine;
import pl.edu.icm.cermine.structure.model.BxPage;
import pl.edu.icm.cermine.tools.timeout.TimeoutException;

public class CerminePdf {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(CerminePdf.class);

	Path file = Paths.get(Constants.outputFileDir + "result.txt");

	private ContentExtractor extractor;
	private InputStream inputStream;
	private HashMap<Integer, String> lines;
	private HashMap<Integer, String> probableLocationLines;
	private static ArrayList<Locale> locale;
	private BxDocument bxDocument;
	private ArrayList<BxPage> bxPages;

	public CerminePdf(String fileName) throws AnalysisException, IOException {

		bxDocument = new BxDocument();
		bxPages = new ArrayList<BxPage>();
		extractor = new ContentExtractor();
		inputStream = new FileInputStream(fileName);
		extractor.setPDF(inputStream);

		// for (BxPage page : extractor.getBxDocument()) {
		// bxPages.add(page);
		// }
		// logger.debug("Page length: " + bxPages.size());
		// bxDocument = extractor.getBxDocument().setPages(new
		// ArrayList<>(bxPages));
		// extractor.setTimeout(9999);
		locale = Constants.getCountryNames();
	}

	/*
	 * Extract all text as line by line from the pdf
	 */
	public HashMap<Integer, String> getAllLines() throws TimeoutException, AnalysisException {
		lines = new HashMap<Integer, String>();

		Iterator<BxLine> iline = extractor.getBxDocument().asLines().iterator();
		int lineNo = 1;
		while (iline.hasNext()) {
			lines.put(lineNo, iline.next().toText());
			logger.debug("lineNo: " + lineNo + "\t" + iline.next().toText());
			lineNo++;
		}
		return lines;
	}

	public HashMap<Integer, String> getProbableLocationLines() {
		probableLocationLines = new HashMap<Integer, String>();

		if (this.lines != null && !this.lines.isEmpty()) {

			for (int lineNo : this.lines.keySet()) {
				for (Locale country : locale) {
					if (this.lines.get(lineNo).contains(country.getDisplayCountry())) {
						probableLocationLines.put(lineNo, lines.get(lineNo));
					}
				}
			}
		}

		return probableLocationLines;
	}
	
	

	// public // Element result = extractor.getNLMContent();
	// // String rawResult = extractor.getRawFullText();
	// // Element resultLabeledFullText =
	// // extractor.getLabelledRawFullText();
	//
	// Iterator<BxLine> iline = extractor.getBxDocument().asLines().iterator();
	// Iterator<BxZone> izone = extractor.getBxDocument().asZones().iterator();
	//
	// PrintWriter writer = new PrintWriter(Constants.outputFileDir +
	// "result.txt");
	//
	// while (iline.hasNext()) {
	// writer.println(iline.next().toText());
	// logger.debug("text" + iline.next().toText());
	// }
	// writer.close();

	public DocumentMetadata getMetadata() throws TimeoutException, AnalysisException {
		// DocumentMetadata metadata = extractor.getMetadata();
		//
		// System.out.println("###########Title: " + metadata.getTitle());
		//
		// System.out.println("First page: " + metadata.getFirstPage() + "\t
		// Last Page: " + metadata.getLastPage());
		// System.out.println();
		
		return extractor.getMetadata();
	}

	public static void main(String[] args) {

		try {
			CerminePdf cPdf = new CerminePdf(Constants.testPdfName);
			cPdf.getAllLines();
		} catch (AnalysisException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// try (Stream<Path> paths =
		// Files.walk(Paths.get(Constants.testFileDir))) {
		// paths.forEach(filePath -> {
		// if (Files.isRegularFile(filePath)) {
		//
		// if (filePath.toString().endsWith(".pdf")) {
		//
		// new CerminePdf(filePath.toString());
		// } else {
		// System.out.println(filePath.getFileName().toString() + " not a pdf
		// file.");
		// }
		// }
		//
		// });
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}
}
