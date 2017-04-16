/**
 * 
 */
package edu.wright.dase.cs.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;


import edu.wright.cs.Location;
import edu.wright.cs.LocationPageRange;
import edu.wright.cs.PageRange;
import edu.wright.cs.util.Constants;
import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.cermine.metadata.model.DocumentMetadata;

/**
 * @author sarker
 *
 */
public class LocationPageRangeTest {

	LocationPageRange locationPageRange;

	private static Location location;
	private static PageRange pageRange;
	private static DocumentMetadata metadata;

	String loc = "Beijing, China";
	String PAGERANGE = "181-190";
	String testPdfForPageRange = "/target/test-classes/JUnitTestPDF/v14p0181.pdf";
	String testPdfForLocation = "/target/test-classes/JUnitTestPDF/geisterGame.pdf";

	@Before
	public void initialize() {

		String runningDir = System.getProperty("user.dir");
		testPdfForPageRange = runningDir + testPdfForPageRange;
		testPdfForLocation = runningDir + testPdfForLocation;
		locationPageRange = new LocationPageRange();

	}

	/**
	 * Test method for
	 * {@link edu.wright.cs.LocationPageRange#initialize(java.nio.file.Path)}.
	 */
	@Test
	public void testInitialize() {
		// test for regular pdf
		try {
			locationPageRange.initialize(Paths.get(Constants.testPdfName));

			pageRange = locationPageRange.getPageRangeObj();
			metadata = locationPageRange.getMetadataObj();
			location = locationPageRange.getLocationObj();

			assertNotNull("PageRange Must not be null", pageRange);
			assertNotNull("metadata Must not be null", metadata);
			assertNotNull("iTextPdf Must not be null", location);

		} catch (AnalysisException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// test for password protected pdf
		// this must throw exception
		try {
			locationPageRange.initialize(Paths.get(Constants.testPdfNamePP));
			fail("If this executes then exception is not thrown");
		} catch (Exception e) {

		}
	}

	/**
	 * Test method for
	 * {@link edu.wright.cs.LocationPageRange#extractPageRange()}.
	 * 
	 * @throws IOException
	 * @throws AnalysisException
	 */
	@Test
	public void testExtractPageRange() throws AnalysisException, IOException {

		locationPageRange.initialize(Paths.get(testPdfForPageRange));
		assertEquals("PAGERANGE must match", locationPageRange.extractPageRange(), PAGERANGE);

	}

	/**
	 * Test method for
	 * {@link edu.wright.cs.LocationPageRange#extractLocation()}.
	 * 
	 * @throws IOException
	 * @throws AnalysisException
	 */
	@Test
	public void testExtractLocation() throws AnalysisException, IOException {

		locationPageRange.initialize(Paths.get(testPdfForLocation));
		String __loc = locationPageRange.extractLocation();
		System.out.println("location: " + __loc);
		assertEquals("Location must match", locationPageRange.extractLocation(), loc);

	}

}
