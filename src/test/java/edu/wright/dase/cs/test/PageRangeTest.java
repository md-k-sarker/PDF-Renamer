/**
 * 
 */
package edu.wright.dase.cs.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.hamcrest.core.AnyOf;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;

import edu.wright.cs.PageRange;
import pl.edu.icm.cermine.exception.AnalysisException;

/**
 * @author sarker
 *
 */
public class PageRangeTest {

	String PAGERANGE = "181-190";
	String testPdfForPageRange = "/target/test-classes/JUnitTestPDF/v14p0181.pdf";

	String pageRangeNotFound = "Page Range not found.";
	PageRange pageRange;

	@Before
	public void initialize() throws AnalysisException, IOException {
		String runningDir = System.getProperty("user.dir");
		testPdfForPageRange = runningDir + testPdfForPageRange;
		pageRange = new PageRange(testPdfForPageRange);
		assertNotNull("PageRange Object must not be null", pageRange);
	}


	/**
	 * Test method for {@link edu.wright.cs.PageRange#getPageRange()}.
	 */
	@Test
	public void testGetPageRange() {
		assertEquals("PageRange must match.", PAGERANGE, pageRange.getPageRange());
	}

}
