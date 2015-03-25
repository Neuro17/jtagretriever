package app.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;


/**
 * @author biagio
 *
 */
@Component
public class Tokenizer {
	
	private static final String STOP_WORDS_FILE =  "stop_words.txt";
	private static final Logger log = LogManager.getLogger(Tokenizer.class);
	
	/**
	 * It takes a list of sentences and returns a Map where key is a word and 
	 * value is the occurrency of that word.
	 * 
	 * @param sentences: ArrayList of text to tokenize
	 * @return 
	 * @throws IOException
	 */
	public static Map<String, Integer> tokenize(ArrayList<String> sentences) {
		
		Map<String, Integer> countWords = new HashMap<String, Integer>();
		ArrayList<String> stopWords = Tools.readFileFromResource(STOP_WORDS_FILE);
//		Set<String> stopWords = new LinkedHashSet<String>();;
		
		for(String str : sentences){
			if(str != null) {
				str = str.toLowerCase();
				StringTokenizer tokenizer = new StringTokenizer(str);		
				while(tokenizer.hasMoreElements()){
					
					String el = tokenizer.nextToken();
					
					if(!stopWords.contains(el)){
						if(countWords.containsKey(el))
							countWords.put(el, countWords.get(el) + 1);
						else
							countWords.put(el, 1);
					}
				}
			}
			else {
				log.error("String is null");
			}
			
		}
		return countWords;
	}

}
