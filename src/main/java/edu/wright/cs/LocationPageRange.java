package edu.wright.cs;

import java.io.IOException;
import java.nio.file.Path;

import org.slf4j.LoggerFactory;

import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.cermine.metadata.model.DocumentMetadata;

/**
 * 
 * @author sarker
 *
 */

public class LocationPageRange {

	private  org.slf4j.Logger logger = LoggerFactory.getLogger(LocationPageRange.class);

	private  Location location;
	private  PageRange pageRange;
	private  DocumentMetadata metadata;

	/**
	 * @return the location object
	 */
	public  Location getLocationObj() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public  void setLocationObj(Location location) {
		this.location = location;
	}

	/**
	 * @return the pageRange
	 */
	public  PageRange getPageRangeObj() {
		return pageRange;
	}

	/**
	 * @param pageRange
	 *            the pageRange to set
	 */
	public  void setPageRangeObj(PageRange pageRange) {
		this.pageRange = pageRange;
	}

	/**
	 * @return the metadata
	 */
	public  DocumentMetadata getMetadataObj() {
		return metadata;
	}

	/**
	 * @param metadata
	 *            the metadata to set
	 */
	public  void setMetadataObj(DocumentMetadata metadata) {
		this.metadata = metadata;
	}

	/**
	 * Initialize location object and Cermine pdf Object.
	 * 
	 * @param path
	 * @throws IOException
	 * @throws AnalysisException
	 * @pre path is not null
	 * @post pageRange, location is not null
	 */
	public void initialize(Path path) throws AnalysisException, IOException {

		//assert path != null;
		pageRange = new PageRange(path.toString());

		//assert pageRange != null;

		metadata = pageRange.getMetadata();
		location = new Location(path.toString());
		location.setAuthorNames(metadata.getAuthors());

		//assert location != null;

	}

	/**
	 * Extract Page Range if possible. If not possible then return N/A
	 * 
	 * @return PageRange of the pdf.
	 * @pre metadata is not null
	 */
	public String extractPageRange() {
		logger.debug("extractPageRange() starts");
		assert metadata != null;

		String fP = metadata.getFirstPage();
		String lP = metadata.getLastPage();
		logger.debug("first: " + fP + " last: " + lP);
		if (fP != null && lP != null) {
			System.out.println("Page Range: " + fP + "-" + lP);
			return (fP + "-" + lP);

		} else {
			System.out.println("Page Range: not found.");
			return ("N/A");
		}

	}

	/**
	 * Extract location from the pdf
	 * 
	 * @return location of the pdf
	 * @throws IOException
	 * @pre location is not null
	 */
	public String extractLocation() throws IOException {
		assert location != null;
		logger.debug("extractLocation() starts");
		location.selectFirstPageTexts();
		location.selectProbaleTextForLocation();
		/*
		 * to-do need to fix it
		 */
		// location.verifyProbableLocation();

		String loc = location.extractLocationFromProbableLines();
		if (loc.length() > 1) {
			System.out.println("Location: " + loc);
			return loc;
		} else {
			System.out.println("Location: not found.");
			return "N/A";
		}
		// logger.debug("extractLocation() ends");
	}

	/**
	 * For testing purpose only
	 * 
	 * @param args
	 */
	public  void main(String[] args) {

		for (int i = 0; i < args.length; i++) {
			logger.debug("command line args[" + i + "]\t" + args[i]);
		}

	}

}
