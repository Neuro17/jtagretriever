package lucene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TokenikeStopStem {
	
	private static final Logger log = LogManager.getLogger(TokenikeStopStem.class);
	
	public Set<String> getStopWords(){
		Set<String> stopWords = new LinkedHashSet<String>();
		
		String line = "";
		
		InputStream inputStream =  this.getClass().getResourceAsStream("../resource/stop_words.txt");
		
        BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
        
        try {
			while (( line = rd.readLine()) != null) {
		    stopWords.add(line);
//				log.debug(line);
			}
			rd.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
        return stopWords;
	}
	
	public Map<String, Integer> tokenize(ArrayList<String> tweetsText) throws IOException{
		Map<String, Integer> countWords = new HashMap<String, Integer>();
		Set<String> stopWords = getStopWords();
		
		for(String str : tweetsText){
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
		return countWords;
	}
	
	
	public static void main(String[] args) throws IOException{
		ArrayList<String> list = new ArrayList<String>();
		list.add("I am Biagio");
		Map<String, Integer> countWords = new TokenikeStopStem().tokenize(list);
		for (Map.Entry<String, Integer> entry : countWords.entrySet()) {
		    log.debug(entry.getKey() + " : " + entry.getValue());
		}
	}
	
}
