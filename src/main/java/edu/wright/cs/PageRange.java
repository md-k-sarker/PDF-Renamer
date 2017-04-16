/**
 * 
 */
package edu.wright.cs;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.LoggerFactory;

import edu.wright.cs.util.Constants;
import pl.edu.icm.cermine.ContentExtractor;
import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.cermine.metadata.model.DocumentMetadata;
import pl.edu.icm.cermine.tools.timeout.TimeoutException;

/**
 * @author sarker
 *
 */
public class PageRange {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(PageRange.class);

	Path file = Paths.get(Constants.outputFileDir + "result.txt");

	private ContentExtractor extractor;
	private InputStream inputStream;
	private DocumentMetadata metadata;

	/**
	 * Constructor
	 * 
	 * @param fileName
	 *            of the pdf
	 * @throws AnalysisException
	 * @throws IOException
	 */
	public PageRange(String fileName) throws AnalysisException, IOException {

		this.extractor = new ContentExtractor();
		this.inputStream = new FileInputStream(fileName);
		this.extractor.setPDF(inputStream);
		this.metadata = this.extractor.getMetadata();

	}

	public DocumentMetadata getMetadata() throws TimeoutException, AnalysisException {
		return this.metadata;
	}

	/**
	 * 
	 * @return
	 * @pre metadata != null
	 */
	public String getPageRange() {
		assert this.metadata != null;
		String firstPage = this.metadata.getFirstPage();
		String lastPage = this.metadata.getLastPage();
		if (firstPage == null || lastPage == null)
			return "Page Range not found.";
		return firstPage + "-" + lastPage;
	}

	/**
	 * For test purpose only
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			PageRange cPdf = new PageRange(Constants.testPdfName);
		} catch (Exception e) {

		}
	}
}