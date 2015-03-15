package server.tag.extractor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Tokenizer {
	
	private static final Logger log = LogManager.getLogger(Tokenizer.class);
	private static final String HOME_PATH = System.getProperty("user.dir");
	private static final String RESOURCE_PATH = HOME_PATH.concat("/src/main/resources/stop_words.txt");
	
	public Set<String> getStopWords() throws IOException{
		Set<String> stopWords = new LinkedHashSet<String>();
				
		log.debug(RESOURCE_PATH);
			
		log.debug(System.getProperty("user.dir"));
		
        
		for (String newLine : Files.readAllLines(Paths.get(RESOURCE_PATH), Charset.defaultCharset())) {
//		    log.debug(newLine);
		    stopWords.add(newLine);
		}
	
        return stopWords;
	}
	
	@SuppressWarnings("unused")
	private void listFiles(){
		File folder = new File(System.getProperty("user.dir").concat("/src/test/resources"));
		File[] listOfFiles = folder.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		        System.out.println("File " + listOfFiles[i].getName());
		      } else if (listOfFiles[i].isDirectory()) {
		        System.out.println("Directory " + listOfFiles[i].getName());
		      }
		    }
	}
	
	public Map<String, Integer> tokenize(ArrayList<String> tweetsText) throws IOException{
		Map<String, Integer> countWords = new HashMap<String, Integer>();
		Set<String> stopWords = getStopWords();
//		Set<String> stopWords = new LinkedHashSet<String>();;
		
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

}
