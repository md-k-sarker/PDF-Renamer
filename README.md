## PDF-Renamer

### How to run:

<ol>
<li> Download the executable jar <a href="https://github.com/md-k-sarker/PDF-Renamer/releases/download/v1.1.0/pdfRenamer.jar" title="PDF Renamer"> pdfRenamer.jar </a> </li>

<li> Run jar with the following cmd: 
<ul>
<li>java -jar pdfRenamer.jar -flag %renameFormat path/to/your/pdf </li>
</ul>
</ol>

####Details about how to run the program 

Commands:  pdfRenamer -flag renameFormat Name

-flag: indicates file or directory.
  -f: indicates file 
  -d: indicates directory. Will do batch operation to all files with .pdf extension. Will not search recursively for inner directory.

renameFormat:
  %au - authors.
  %is - issueNo. 
  %lo - location.
  %pr - page range.
  %pu - publisher.
  %yr - year.		  %n  - white space		  %anyCharacter - insert any character
		  Enter format without spaces in between.
	
 Name: Name of directory or file.
		  Multiple file name seperated by space can be given.
		  Multiple directory name is not permitted.


### Functionality:

This jar extracts:
<ol>
 <li>Author Names </li>
  <li>Issue Number </li>
 <li>Publisher Name </li>
  <li>Publish Year </li>
   <li>Page range </li>
  <li>Location of the conference </li>
 </ol>
from of a research paper. 



<a href="http://cecs.wright.edu/~pmateti/Courses/7140/Projects/7140-2017-spring-project.html" title="Course project">Course project</a> of Advanced Software Engineering course <a href="http://cecs.wright.edu/~pmateti/Courses/7140/Top/index.html" title="Advanced Software Engineering"> Advanced Software Engineering</a> at Wright State University.





