package edu.wright.cs.util;

import java.util.ArrayList;
import java.util.Locale;

import org.slf4j.LoggerFactory;

public class Constants {

	public static String testPdfName = "/Users/sarker/WorkSpaces/EclipseNeon/PDF-Renamer/testpdfs_backup/1-s2.0-S0167923605000576-main.pdf";

	public static String testPdfNamePP = "/Users/sarker/WorkSpaces/EclipseNeon/PDF-Renamer/testpdfs_backup/password protected.pdf";

	public static String testFileDir = "/Users/sarker/WorkSpaces/EclipseNeon/PDF-Renamer/src/main/resources/pdfs/testpdf/";

	public static String outputFileDir = "/Users/sarker/WorkSpaces/EclipseNeon/PDF-Renamer/result/";

	public static ArrayList<Locale> countryNames = new ArrayList<Locale>();

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(Constants.class);

	public static ArrayList<Locale> getCountryNames() {
		String[] cCodes = Locale.getISOCountries();

		for (String cCode : cCodes) {
			Locale country = new Locale("", cCode);
			countryNames.add(country);
		}

		String[] arr = { "USA", "UAE", "UK" };
		for (String a : arr) {
			Locale country = new Locale("", a);
			countryNames.add(country);
		}

		return countryNames;
	}

	public static void main(String[] args) {
		ArrayList<Locale> countryNames = getCountryNames();
		for (Locale l : countryNames) {
			logger.debug("country: " + l.getCountry() + "\t name: " + l.getDisplayName());
		}
	}
}
