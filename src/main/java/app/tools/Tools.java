package app.tools;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Tools {
	
	private static final Logger log = LogManager.getLogger(Tools.class);
	public static final String HOME_PATH = System.getProperty("user.dir");
	public static final String RESOURCE_PATH = HOME_PATH.concat("/src/main/resources/");
	
	public static ArrayList<String> readFileFromResource(String fileName, String skip ){
		ArrayList<String> lines = new ArrayList<String>();
		String file = RESOURCE_PATH.concat(fileName);
		boolean firstLine  = true;
		
		log.debug(file);
		
		try {
			for (String newLine : Files.readAllLines(Paths.get(file), 
					Charset.defaultCharset())) {
				if(! (newLine.startsWith(skip) && firstLine) ) {
					lines.add(newLine);	
				}
				else {
					firstLine = false;
				}
			}
		} catch (IOException e) {
			log.debug(e.getMessage());
			e.printStackTrace();
		}
		
		return lines;
	}
	
	public static ArrayList<String> readFileFromResource(String fileName){
		ArrayList<String> lines = new ArrayList<String>();
		String file = RESOURCE_PATH.concat(fileName);
		
		log.debug(file);
		
		try {
			for (String newLine : Files.readAllLines(Paths.get(file), 
					Charset.defaultCharset())) {
				
				lines.add(newLine);	
			}
		} catch (IOException e) {
			log.debug(e.getMessage());
			e.printStackTrace();
		}
		
		return lines;
	}
	
	/**
	 * Utility method that lists the directory files.
	 * 
	 */
	@SuppressWarnings("unused")
	private void listFiles(String path){
		File folder = new File(path);
		
		File[] listOfFiles = folder.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) {
		    	if (listOfFiles[i].isFile()) {
		    		System.out.println("File " + listOfFiles[i].getName());
		    	} else if (listOfFiles[i].isDirectory()) {
		    		System.out.println("Directory " + listOfFiles[i].getName());
		    	}
		    }
	}
	
	public static void main(String [] args) {
		String file = "artists.txt";
		
		for(String line : Tools.readFileFromResource(file, "#")) {
			log.debug(line);
		}
	}

}


