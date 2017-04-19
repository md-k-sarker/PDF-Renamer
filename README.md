## PDF-Renamer

### How to run:

<ol>
<li> Download the executable jar <a href="https://github.com/md-k-sarker/PDF-Renamer/releases/download/v1.1.0/pdfRenamer.jar" title="PDF Renamer"> pdfRenamer.jar </a> </li>

<li> Run jar with the following cmd: 
<ul>
<li>java -jar pdfRenamer.jar -flag %renameFormat path/to/your/pdf </li>
</ul>
<ul>Example: 
<li>java -jar pdfRenamer.jar -d %au%is%lo%pr%pu%yr user/xxxx/testpdfs/ </li>
<li> This will rename the pdfs in testpdfs directory</li>
</ul>
</ol>

### Details about how to run the program 

#### Command:  
#### pdfRenamer -flag renameFormat Name

<ul>-flag: indicates file or directory.
<li> -f: indicates file </li>
 <li> -d: indicates directory. Will do batch operation to all files with .pdf extension. Will not search recursively for inner directory.</li></ul>

<ul>renameFormat:
  <li>%au - authors.</li>
  <li>%is - issueNo. </li>
  <li>%lo - location.</li>
  <li>%pr - page range.</li>
  <li>%pu - publisher.</li>
  <li>%yr - year.</li>		  <li>%n  - white space</li>		  <li>%anyCharacter - insert any character</li>
  </ul>
		  Enter format without spaces in between.
	
 <ul>Name: Name of directory or file.
		<li>  Multiple file name seperated by space can be given.</li>
		<li> Multiple directory name is not permitted.</li> </ul>


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





