package core.nlp;

import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor;

public class PreProcessor implements SentencePreProcessor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3153903017807212908L;

	@Override
	public String preProcess(String sentence) {
		System.err.println(sentence);
		return sentence.toLowerCase().replaceAll("[^a-z ]", "");
		
	}

}
