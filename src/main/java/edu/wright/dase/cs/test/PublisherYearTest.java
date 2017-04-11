package edu.wright.dase.cs.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.junit.Test;

import edu.wright.cs.PublisherYear;

public class PublisherYearTest {

	@Test
	public void testIsMonthNear() {
		
		String[] wordTest = { "JANUARY", "April", "2016", "4321a" }; 
		
		//Check for index 0.
		assertFalse(PublisherYear.isMonthNear(wordTest, 0));
		
		//Check for April : Should return true 
		assertTrue(PublisherYear.isMonthNear(wordTest, 1));
		
		//Check for 2016  : Should return true
		assertTrue(PublisherYear.isMonthNear(wordTest, 2));
		
		//Check for 4321 : Should return false
		assertFalse(PublisherYear.isMonthNear(wordTest, 3));	
		
	}

	@Test
	public void testInitialize() {
		//fail("Not yet implemented");
	}

	@Test
	public void testIsPublish() {
		
		
String[] wordsTest	= {	"Published" , "June" , "2016", "IEEE", "asdas" , "dasdsa",
						"publish" , "1986", "asds", "asdsa" , "dasdas" ," adsas",
						"published","on", "June", "2016" };
String[] wordsTest2 = { "Hello", "Hi", "How", "are", "you" };

//Search term at 1st line
assertTrue(PublisherYear.isPublish(wordsTest, 2));

//Search term at random position in middle
assertTrue(PublisherYear.isPublish(wordsTest, 7));

//for i == arraylength-1
assertTrue(PublisherYear.isPublish(wordsTest, 13));

//for a token which doesn't have search terms near it.
assertFalse(PublisherYear.isPublish(wordsTest2, 4));
		
	}

	@Test
	public void testIsNumeric() {
		
		String[] testWords = { "123" , "123adg", "2021", "2016" };
		
		// Check for valid numbers that are => 1500 && <= currentYear
		assertFalse(PublisherYear.isNumeric(testWords[0]));
		
		//Check for token if it is strictly numeric
		assertFalse(PublisherYear.isNumeric(testWords[1]));
		
	
		//Check if numeric and not greater than current year.
		assertFalse(PublisherYear.isNumeric(testWords[2]));
		
		//Check for valid year 
		assertTrue(PublisherYear.isNumeric(testWords[3]));
	}

	@Test
	public void testGetFiles() {
		//Redundant function in merged project.
	}


	@SuppressWarnings("deprecation")
	@Test
	public void testGetText() {
		String path1 = "src/main/resources/JUnitTestPDF/TestPDF1.pdf";
		String path2 = "src/main/resources/JUnitTestPDF/TestPDFempty.pdf";
		PDDocument doc1,doc2;
		try {
			
			//Check for PDF's with content
			doc1 = PDDocument.load(Paths.get(path1).toFile());
			String testText1 = PublisherYear.getText(doc1);
			String[] testTextArray1 = testText1.split("\\s+");
			String[] expectedTestArray1 = {"Test","for","content"};
			//System.out.println(testText1 +" length ="+ testText1.length());
			assertEquals(testTextArray1,expectedTestArray1);
			
			//Check for PDF's without any content
			doc2= PDDocument.load(Paths.get(path2).toFile());
			String testText2 = PublisherYear.getText(doc2);
			String[] testTextArray2 = testText2.split("\\s+");
			String[] expectedTestArray2 = {};
			//System.out.println(testText2+"length="+testText2.length());
			assertEquals(testTextArray2,expectedTestArray2);
			
		} catch (InvalidPasswordException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	@Test
	public void testGetYears() {
		PublisherYear testObject = new PublisherYear();
		
		testObject.setFullText("This was Published on July 2015");
		assertEquals("July 2015",testObject.getYears());
		
		testObject.setFullText("Accepted July 2015 and Published on Aug 2015");
		assertEquals("Aug 2015",testObject.getYears());
		
		testObject.setFullText("2016 IEEE");
		assertEquals("2016",testObject.getYears());
		
		testObject.setFullText("2017 Kluver");
		assertEquals("2017",testObject.getYears());
		
		testObject.setFullText("Published on July 2222");
		assertEquals("N/A",testObject.getYears());
		
		testObject.setFullText("This does not have a publish year");
		assertEquals("N/A",testObject.getYears());
		
		
	}


	@Test
	public void testGetPublisher() {
		//fail("Not yet implemented");
	}



}
