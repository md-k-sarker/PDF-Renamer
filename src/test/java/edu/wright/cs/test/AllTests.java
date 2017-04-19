/**
 * 
 */
package edu.wright.cs.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author sarker
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ LocationPageRangeTest.class, LocationTest.class, PageRangeTest.class, pdfRenamerTest.class,
		PublisherYearTest.class, TestAuthorIssue.class })
public class AllTests {

}
