package core.nlp;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.deeplearning4j.models.embeddings.WeightLookupTable;
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.models.word2vec.wordstore.VocabCache;
import org.deeplearning4j.models.word2vec.wordstore.inmemory.AbstractCache;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import core.utils.ExceptionHandler;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Trainer {
	
	private final String MODEL_PATH = "KittyBotModel.txt";
	
	PreProcessor preProcessor;
	List<List<String>> partitioned;
	
	
	private Word2Vec model;

	//TODO: Move to properties file
	private final int minWordFrequency = 5;
	private final int layerSize = 100;
	private final long seed = 42;
	private final int windowSize = 5;
	
	private SentenceIterator iter;
	private TokenizerFactory tokenizer;

	private int TOP_N = 5;
	
	public void train(MessageReceivedEvent e) {
		
		try {
			System.out.println("Loading model...");
			if(model == null) {
				loadModel();
				tokenizer = new DefaultTokenizerFactory();
		        tokenizer.setTokenPreProcessor(new CommonPreprocessor());
		        
		        model.setTokenizerFactory(tokenizer);
			}
			System.out.println("Model loaded.");
		} catch(IOException e1) {
			System.out.println("Building fresh model...");
			model = buildModel();
		}
        model.setSentenceIterator(iter);

		System.out.println("Training...");
		try {
			model.fit();
		} catch (Exception e1) {
			ExceptionHandler.printErr(e1);
			return;
		}
		System.out.println("Training complete.");
		
		System.out.println("Saving model...");
		WordVectorSerializer.writeWord2VecModel(model, MODEL_PATH);
		System.out.println("Model saved.");
		
	}
	
	public void loadModel() throws IOException {
		File modelFile = new File(MODEL_PATH);
		if (!modelFile.exists()) {
			throw new IOException();
		}
		
		model = WordVectorSerializer.readWord2VecModel(MODEL_PATH);

	}
	
	public Word2Vec buildModel() {
		tokenizer = new DefaultTokenizerFactory();
        tokenizer.setTokenPreProcessor(new CommonPreprocessor());
        VocabCache<VocabWord> cache = new AbstractCache<>();
        WeightLookupTable<VocabWord> table = new InMemoryLookupTable.Builder<VocabWord>()
                .vectorLength(100)
                .useAdaGrad(false)
                .cache(cache).build();
		return new Word2Vec.Builder()
				.minWordFrequency(minWordFrequency)
				.layerSize(layerSize)
				.seed(seed)
				.windowSize(windowSize)
				.tokenizerFactory(tokenizer)
				.lookupTable(table)
				.vocabCache(cache)
				.build();
		}
	
	public void setDataSource(List<String> messages) {
		iter = new CollectionSentenceIterator(new PreProcessor(), messages);
	}

	public boolean closeToCat(String word) {
		Collection<String> results = findClosest(word);
		for(String result : results) {
			if (result.equals("cat")) return true;
		}
		return false;
	}
	
	public Collection<String> findClosest(String word){
		try {
			if(model == null)
				loadModel();
			return model.wordsNearestSum(word, TOP_N);
		} catch (IOException e) {
			ExceptionHandler.printErr(e);
		}
		return null;

	}


}
