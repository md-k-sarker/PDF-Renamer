/**
 * 
 */
package edu.wright.dase.cs.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.itextpdf.text.pdf.PdfReader;

import edu.wright.cs.Location;
import edu.wright.cs.PageRange;

/**
 * @author sarker
 *
 */
public class LocationTest {

	String actualLoc = "Beijing, China";
	String PAGERANGE = "181-190";
	String testPdfForPageRange = "/target/test-classes/JUnitTestPDF/v14p0181.pdf";
	String testPdfForLocation = "/target/test-classes/JUnitTestPDF/geisterGame.pdf";

	/*
	 * Static removed after findbug found it.
	 */
	private Location location;
	private PageRange pageRange;
	private int actualNoOfPage = 8;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void initialize() throws Exception {

		String runningDir = System.getProperty("user.dir");
		testPdfForPageRange = runningDir + testPdfForPageRange;
		testPdfForLocation = runningDir + testPdfForLocation;

		pageRange = new PageRange(testPdfForPageRange);
		location = new Location(testPdfForLocation);

		location.setAuthorNames(pageRange.getMetadata().getAuthors());

	}

	/**
	 * Test method for {@link edu.wright.cs.Location#getTotalPage()}.
	 */
	@Test
	public void testGetTotalPage() {

		int noOfPage = location.getTotalPage();
		assertEquals("Page number must be equal", actualNoOfPage, noOfPage);
	}

	/**
	 * Test method for {@link edu.wright.cs.Location#selectFirstPageTexts()}.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testSelectFirstPageTexts() throws IOException {
		location.selectFirstPageTexts();

		assertTrue("length of line must be greater than 0", location.getLines().size() > 0);
	}

	/**
	 * Test method for
	 * {@link edu.wright.cs.Location#selectProbaleTextForLocation()}.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testSelectProbaleTextForLocation() throws IOException {
		location.selectFirstPageTexts();
		location.selectProbaleTextForLocation();
		assertTrue("Probable text location lines must be less than first page text lines",
				location.getProbableLocation().size() < location.getLines().size());
	}

	/**
	 * Test method for
	 * {@link edu.wright.cs.Location#extractLocationFromProbableLines()}.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testExtractLocationFromProbableLines() throws IOException {
		location.selectFirstPageTexts();
		location.selectProbaleTextForLocation();
		String loc = location.extractLocationFromProbableLines();
		assertEquals("Location must match", actualLoc, loc);
	}

}
