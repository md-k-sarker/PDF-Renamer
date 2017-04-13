/**
 * 
 */
package edu.wright.dase.cs.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import edu.wright.cs.LocationPageRange;
import edu.wright.cs.cermine.CerminePdf;
import edu.wright.cs.itext.ITextPdf;
import edu.wright.cs.util.Constants;
import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.cermine.metadata.model.DocumentMetadata;

/**
 * @author sarker
 *
 */
public class LocationPageRangeTest {

	private static ITextPdf iTextPdf;
	private static CerminePdf cerminePdf;
	private static DocumentMetadata metadata;
	LocationPageRange locationPageRange;

	@Before
	public void initialize() {

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

			iTextPdf = locationPageRange.getiTextPdf();
			cerminePdf = locationPageRange.getCerminePdf();
			metadata = locationPageRange.getMetadata();
			assertNotNull("CerminePdf Must not be null", cerminePdf);
			assertNotNull("metadata Must not be null", metadata);
			assertNotNull("iTextPdf Must not be null", iTextPdf);

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
	 * {@link edu.wright.cs.LocationPageRange#LocationPageRange()}.
	 */
	@Test
	public void testLocationPageRange() {
		// fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link edu.wright.cs.LocationPageRange#extractPageRange()}.
	 */
	@Test
	public void testExtractPageRange() {
		// fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link edu.wright.cs.LocationPageRange#extractLocation()}.
	 */
	@Test
	public void testExtractLocation() {
		// fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link edu.wright.cs.LocationPageRange#main(java.lang.String[])}.
	 */
	@Test
	public void testMain() {
		// fail("Not yet implemented");
	}

}
