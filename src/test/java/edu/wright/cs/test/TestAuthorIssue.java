package edu.wright.cs.test;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.wright.cs.AuthorIssue;


public class TestAuthorIssue {

	private static  String pdfName = "/target/test-classes/JUnitTestPDF/00895976.pdf";
	private static  String invalidFileName = "/target/test-classes/JUnitTestPDF/1603.02754.pdf";
	private static File pdf;
	private static File invalidFile;
	private static String expectedAuthors = null;
	private static String expectedIssueNo = null;
	private static String NOT_APPLICABLE=null;

	@BeforeClass
	public static void  runBeforeTestMethods() {

		//updated 
		String runningDir = System.getProperty("user.dir");
		pdfName = runningDir + pdfName;
		invalidFileName = runningDir + invalidFileName;
		
		pdf = new File(pdfName);
		invalidFile = new File(invalidFileName);
		NOT_APPLICABLE="N/A";
		expectedAuthors = "Maja Pantic,Leon J.M. Rothkrantz";
		expectedIssueNo = "12";
		
	}

	@Test
	public void testExtractAuthorsNotNull() {
		AuthorIssue tai = new AuthorIssue();
		try {
			String author = tai.extractAuthors(pdf);
			Assert.assertNotNull(author);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testExtractAuthors() {
		AuthorIssue tai = new AuthorIssue();
		try {
			String author = tai.extractAuthors(pdf);
			Assert.assertEquals(expectedAuthors, author);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testEAforInvalidFile(){
		AuthorIssue tai = new AuthorIssue();
		try {
			String author = tai.extractAuthors(invalidFile);
			Assert.assertEquals(NOT_APPLICABLE, author);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testextractIssueNoNotNull() {
		AuthorIssue tai = new AuthorIssue();
		String issue = tai.extractIssueNo(pdf);
		Assert.assertNotNull(issue);
	}
	
	@Test
	public void testextractIssueNo() {
		AuthorIssue tai = new AuthorIssue();
		String issue = tai.extractIssueNo(pdf);
		Assert.assertEquals(expectedIssueNo, issue);
	}
	
	@Test
	public void testEIInvalidFile() {
		AuthorIssue tai = new AuthorIssue();
		String issue = tai.extractIssueNo(invalidFile);
		Assert.assertEquals(NOT_APPLICABLE, issue);
	}

}
