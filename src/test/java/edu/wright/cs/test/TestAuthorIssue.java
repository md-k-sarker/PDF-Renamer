package edu.wright.cs.test;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.wright.cs.AuthorIssue;


public class TestAuthorIssue {

	private static final String pdfName = "00895976.pdf";
	private static final String invalidFileName = "1603.02754.pdf";
	private File pdf;
	private File invalidFile;
	private static String expectedAuthors = null;
	private static String expectedIssueNo = null;
	private static String NOT_APPLICABLE=null;

	@Before
	public void runBeforeTestMethods() {
		ClassLoader classLoader = getClass().getClassLoader();
		pdf = new File(classLoader.getResource(pdfName).getFile());
		invalidFile = new File(classLoader.getResource(invalidFileName).getFile());
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
