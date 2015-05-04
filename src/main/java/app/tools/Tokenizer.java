package app.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

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
		ValueComparator bvc =  new ValueComparator(countWords);
		TreeMap<String,Integer> sortedMap = new TreeMap<String,Integer>(bvc);
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
		sortedMap.putAll(countWords);
		return sortedMap;
	}

}

class ValueComparator implements Comparator<String> {

    Map<String, Integer> base;
    public ValueComparator(Map<String, Integer> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}
