package edu.wright.cs;

import java.io.File;
import java.nio.file.Path;

public class Rename {

	private String Author;
	private String Issue;
	private String Location;
	private String PageRange;
	private String Publisher;
	private String Year;
	private String Doi;
	private Path path;
	private String format;
	private int duplicateCounter = 0;

	public void setFormat(String stringFormat) {
		format = stringFormat;
	}

	public void setPath(Path pdfPath) {
		path = pdfPath;
	}

	public Path getPath() {
		return path;
	}

	public String getIssue() {
		return Issue;
	}

	public void setIssue(String issue) {
		Issue = issue;
	}

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}

	public String getPageRange() {
		return PageRange;
	}

	public void setPageRange(String pageRange) {
		PageRange = pageRange;
	}

	public String getPublisher() {
		return Publisher;
	}

	public void setPublisher(String publisher) {
		Publisher = publisher;
	}

	public String getYear() {
		return Year;
	}

	public void setYear(String year) {
		Year = year;
	}

	public String getDoi() {
		return Doi;
	}

	public void setDoi(String doi) {
		Doi = doi;
	}

	public String getAuthor() {
		return Author;
	}

	public void setAuthor(String author) {
		Author = author;
	}

	protected void renameFile() {
		System.out.println(format);
		String[] renameFormat = format.split("%");
		String filename = "";
		for (String str : renameFormat) {
			System.out.println(str);
			switch (str) {
			case "au":
				if (!Author.toString().equals("N/A"))
					filename += Author;
				break;

			case "is":
				if (!Issue.equals("N/A"))
					filename += Issue;
				break;

			case "lo":
				if (!Location.equals("N/A"))
					filename += Location;
				break;

			case "pr":
				if (!PageRange.equals("N/A"))
					filename += PageRange;
				break;

			case "pu":
				if (!Publisher.equals("N/A"))
					filename += Publisher;
				break;

			case "yr":
				if (!Year.equals("N/A"))
					filename += Year;
				break;
			case "n":
				filename += " ";
				break;
			default:
				filename += str;
			}
		}
		
		//Updated. Removing extra whitespaces.
		filename=filename.trim();
		
		// Rename if one or more field is found.
		if (filename.length() > 0) {
			filename += ".pdf";
			File oldFile = new File(path.toString());
			String parentDir = oldFile.getParent();
			// System.out.println("parentDir : "+parentDir);
			String newFilepath = parentDir + "/" + filename;

			// Path newPath = Paths.get(newFilepath);
			File newFile = new File(newFilepath);
			// If a file with same name exists
			if (newFile.exists()) {
				newFilepath = newFilepath.substring(0, newFilepath.length() - 4) + " " + duplicateCounter++ + ".pdf";
				newFile = new File(newFilepath);
			}

			if (oldFile.renameTo(newFile))
				System.out.println("File renamed to : " + filename);
			else
				System.out.println("Error : Couldn't rename file");

		} else
			System.out.println("Failed to extract requested fields");
	}

}
