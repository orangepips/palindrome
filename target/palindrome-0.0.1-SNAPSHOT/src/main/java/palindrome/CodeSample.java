package palindrome;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;
import nu.xom.Serializer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.primitives.Chars;

/**
 * <p>
 * Designed to work as a command line tool that processes
 * an "-input" file in the following format:
 * </p>
 *
 * <pre>
 * &lt;dataset&gt;
 * 	&lt;data1&gt;Value&lt;/data1&gt;
 * 	&lt;data2&gt;value eulav&lt;/data2&gt;
 * &lt;/dataset&gt;
 * </pre>
 *
 *<p>
 * It looks for the "/dataset/data" elements processing each one
 * to identify if it contains a palindrome sending the results
 * to the "-output" parameter as such:
 *</p>
 *
 *<pre>
 * &lt;answers&gt;
 * 	&lt;answer1&gt;false&lt;/answer1&gt;
 * 	&lt;answer2&gt;true&lt;/answer2&gt;
 * &lt;/answers&gt;
 *</pre>
 *
 *<p><strong>Example</strong></p>
 *<pre>
 *java -jar palindrome-0.0.1-SNAPSHOT-jar-with-dependencies.jar -input ../src/test/resources/dataset.xml -output ./answers.xml
 *</pre>
 *
 * @author m.lesko@verizon.net
 *
 */
public class CodeSample {
	/**
	 * Prefix of data nodes in the input
	 */
	public static final String DATA_PREFIX = "data";

	/**
	 * Prefix of answer nodes in the output
	 */
	public static final String ANSWER_PREFIX = "answer";

	/**
	 * Evaluates a "dataset" XML file to see if each "data" child element contains palindrome text
	 * @see CodeSample.xmlDatasetToXmlAnswers for details
	 * @param args
	 * @throws Exception
	 */
	public static void main(String... args) throws Exception {
		CommandLine cmd = getCommandLine(args);
		if (args.length == 0 || cmd.hasOption("--help")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "CodeSample", getOptions());
			return;
		}
		if (!cmd.hasOption("input") || !cmd.hasOption("output")) {
			System.out.println("'input' and 'output' parameters are required");
			return;
		}
		File datasetFile = new File(cmd.getOptionValue("input"));
		FileOutputStream answersFileOutputStream = new FileOutputStream(new File(cmd.getOptionValue("output")));

		Document answersDocument = xmlDatasetToXmlAnswers(Files.toString(datasetFile, Charsets.UTF_8));
		Serializer answersSerializer = new Serializer(answersFileOutputStream);
		answersSerializer.setIndent(4);
		answersSerializer.write(answersDocument);
	}

	/**
	 * Evaluate passed XML and evaluate if the nodes under "dataset" whose name starts with "data" contain palindromes
	 * Returns a document in the format:
	 * <code>
	 * 	&lt;answers&gt;
	 * 		&lt;answer1&gt;false&lt;/answer1&gt;
	 * 		&lt;answer2&gt;true&lt;/answer2&gt;
	 * 		&lt;answer8&gt;true&lt;/answer8&gt;
	 * 	&lt;/answers&gt;
	 * </code>
	 * Where "answer" is suffixed with whatever follows "data" in the same order the nodes are passed
	 * @param xmlDataset xml dataset to process
	 * @throws Exception
	 */
	public static Document xmlDatasetToXmlAnswers(String xmlDataset) throws Exception {
		Builder builder = new Builder();
		Document datasetDocument = builder.build(new StringReader(xmlDataset));

		Element answersElement = new Element("answers");

		Nodes dataNodes = datasetDocument.query("/dataset/*[starts-with(name(), '" + DATA_PREFIX + "')]");
		for (int i = 0; i < dataNodes.size(); i++) {
			Element dataElement = (Element) dataNodes.get(i);
			Element answerElement = new Element(ANSWER_PREFIX + dataElement.getLocalName().replace(DATA_PREFIX, ""));
			answerElement.appendChild(isPalindrome(dataElement.getValue()).toString());
			answersElement.appendChild(answerElement);
		}

		Document answersDocument = new Document(answersElement);
		return answersDocument;
	}

	/**
	 * Ignoring punctuation, spaces and capitalization determines if the passed sentence is a palindrome.
	 * @param sentence value to evaluate
	 * @return if the sentence is a palindrome.
	 */
	public static Boolean isPalindrome(String sentence) {
		// strip non alphas and make lowercase to make comparison case insensitive
		sentence = sentence.toLowerCase().replaceAll("[^a-z]", "");

		// either half or half + 1, depends if the length is even or odd, respectively
		int halfway = Math.round((float)sentence.length() / 2f);
		String firstHalf = sentence.substring(0, halfway);
		char[] secondHalf = sentence.substring(sentence.length() - halfway).toCharArray();

		// reverse the secondHalf
		// http://stackoverflow.com/questions/2137755/how-do-i-reverse-an-int-array-in-java
		String reversedSecondHalf = Joiner.on("").join(Lists.reverse(Chars.asList(secondHalf)));

		//System.out.println(sentence + "\t" + sentence.length() + "\t" + halfway + "\t" + firstHalf + "\t" + reversedSecondHalf);

		return firstHalf.equals(reversedSecondHalf);
	}

	/**
	 * Create command line parser for input and output files
	 * @return command line parser
	 * @throws ParseException
	 */
	private static CommandLine getCommandLine(String... args) throws ParseException {
		CommandLineParser parser = new PosixParser();
		return parser.parse(getOptions(), args);
	}

	/**
	 * Get options object representing command parameters
	 * @return options object representing command parameters
	 */
	private static Options getOptions() {
		Options options = new Options();
		options.addOption("input", true, "input XML dataset file");
		options.addOption("output", true, "output XML answers file");
		options.addOption("help", false, "print this message");
		return options;
	}
}
