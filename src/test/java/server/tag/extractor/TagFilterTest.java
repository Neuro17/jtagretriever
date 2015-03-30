package server.tag.extractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import app.tools.Tokenizer;


public class TagFilterTest {
	
	private static final Logger log = LogManager.getLogger(TagFilterTest.class);
	
	public static void main(String[] args) throws IOException{
		ArrayList<String> list = new ArrayList<String>();
		
		for(int i = 0; i < 100; i++){
			list.add("I am Biagio at home");
		}
			
		Map<String, Integer> countWords = Tokenizer.tokenize(list);
		for (Map.Entry<String, Integer> entry : countWords.entrySet()) {
		    log.debug(entry.getKey() + " : " + entry.getValue());
		}
	}
	
}
