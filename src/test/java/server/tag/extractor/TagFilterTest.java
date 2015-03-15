package server.tag.extractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TagFilterTest {
	
	private static final Logger log = LogManager.getLogger(TagFilterTest.class);
	
	public static void main(String[] args) throws IOException{
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i < 100; i++){
			list.add("I am Biagio");
		}
			
		Map<String, Integer> countWords = new Tokenizer().tokenize(list);
		for (Map.Entry<String, Integer> entry : countWords.entrySet()) {
		    log.debug(entry.getKey() + " : " + entry.getValue());
		}
	}
	
}
