package palindrome;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Serializer;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

public class TestCodeSample {
//	@Test
//	public void testMain() throws Exception {
//		CodeSample.main("-input", "/Users/mlesko/Documents/workspace-scratch/palindrome/src/test/resources/dataset.xml", "-output", "/Users/mlesko/Documents/workspace-scratch/palindrome/src/test/resources/answers-test.xml");
//	}

	@Test
	public void testXmlDatasetToXmlAnswers() throws Exception {
		InputStream datasetInputStream = this.getClass().getResourceAsStream("/dataset.xml");
		String xmlDataset = CharStreams.toString(new InputStreamReader(datasetInputStream, Charsets.US_ASCII));
		Document actualAnswersDocument = CodeSample.xmlDatasetToXmlAnswers(xmlDataset);

		InputStream answersInputStream = this.getClass().getResourceAsStream("/answers.xml");
		Builder builder = new Builder();
		Document expectedAnswersDocument = builder.build(answersInputStream);

		ByteArrayOutputStream actual = new ByteArrayOutputStream();
		ByteArrayOutputStream expected = new ByteArrayOutputStream();

		Serializer actualSerializer = new Serializer(actual);
		actualSerializer.setIndent(4);
		actualSerializer.write(actualAnswersDocument);

		Serializer expectedSerializer = new Serializer(expected);
		expectedSerializer.setIndent(4);
		expectedSerializer.write(expectedAnswersDocument);

		System.out.println(actual.toString());
		System.out.println(expected.toString());

		assertEquals(actual.toString(), expected.toString());
	}

	@Test
	public void testValidPalindromes() {
		// http://en.wikipedia.org/wiki/Palindrome
		// http://wiki.answers.com/Q/What_is_the_longest_palindrome
		String[] palindromes = {
			"Ada",
			"Anna",
			"Bob",
			"Able was I ere I saw Elba",
			"A man, a plan, a canal - Panama!",
			"Madam, I'm Adam",
			"Madam in Eden, I'm Adam",
			"Doc, note: I dissent. A fast never prevents a fatness. I diet on cod",
			"Never odd or even",
			"Rise to vote, sir",
			"Eva, can I stab bats in a cave?",
			"Mr. Owl ate my metal worm",
			"Was it a car or a cat I saw?",
			"A nut for a jar of tuna",
			"Do geese see God?",
			"Ma is as selfless as I am",
			"Dammit, I'm mad!",
			"A Toyota's a Toyota",
			"Go hang a salami, I'm a lasagna hog",
			"A Santa lived as a devil at NASA",
			"I'm a lasagna hog, go hang a salami."
		};
		for (String palindrome: palindromes) {
			assertTrue(CodeSample.isPalindrome(palindrome));
		}
	}

	@Test
	public void testInvalidPalindromes() {
		// http://en.wikipedia.org/wiki/Palindrome
		// http://wiki.answers.com/Q/What_is_the_longest_palindrome
		String[] palindromes = {
			"Alda",
			"Able was I ere I see Elba",
			"Madam in Eden, I'm Adamant",
			"Doc, note: I dissent. A fast never prevents a fatness. I diet on code",
			"Never odd nor even",
			"Rise to vote, sire",
			"Eva, can I scab bats in a cave?",
			"A Santa lived as a devil at NSA",
		};
		for (String palindrome: palindromes) {
			assertFalse(CodeSample.isPalindrome(palindrome));
		}
	}
}
