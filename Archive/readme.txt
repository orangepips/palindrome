MILLENNIAL MEDIA PALINDROME FINDER CODE SAMPLE

by Matthew Lesko (m.lesko@verizon.net)

The source file of particular interest is palindrome.CodeSample

Designed as a command line tool. Assumes that part of the activity is to
demonstrate not only the ability to write a palindrome algorithm, but also
demonstrate some knowledge about Java libraries.

As such decided to use the following:

 * Maven (http://maven.apache.org/): building the jar
 * XOM (http://www.xom.nu/): XML Parsing Library
 * JUnit (http://www.junit.org/): unit testing framework
 * Guava (http://code.google.com/p/guava-libraries/): collections processing
 * commons-cli (http://commons.apache.org/cli/): command line parser

Usage is as follows:

java -jar palindrome-0.0.1-SNAPSHOT-jar-with-dependencies.jar -input /path/to/dataset.xml -output /path/to/output/answers.xml

NOTES

The example input XML file as-is is not valid XML. I assumed this was an
oversight as opposed to part of the test. You can see the issue in the comment
I've included in the - cleaned up - example below:

<?xml version="1.0"?>
<dataset>
<data1>Don't nod</data1>
<data2>Dogma: I am God</data2>
<data8>Was it Eliot's toilet I saw?</data8>
<data3>Never odd or even</data3>
<data10>How are you era who</data10>
<data4>Too bad Ð I hid a boot</data4>
<data5>Rats live on no evil star</data5>
<data7>No trace; not one carton</data7>
<data9>Murder for a jar of red rum</data9>
<!-- made the assumption that the invalid character "&" was an oversight -->
<!-- <data11>Madam, I&m Adam</data11> -->
<data11>Madam, I'm Adam</data11>
<test>This is not data</test>
</dataset>