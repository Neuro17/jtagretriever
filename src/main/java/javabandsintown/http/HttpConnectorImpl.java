package javabandsintown.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class HttpConnectorImpl {
	private static final Logger log = LogManager.getLogger(HttpConnectorImpl.class);
	private JsonObject jsonResponse;
	protected Gson gson = new GsonBuilder().setPrettyPrinting().create();
	protected URIBuilder uriBld;
	protected URL url;
	protected URI uri;
	
	protected JsonObject getJsonResponse() {
		return jsonResponse;
	}
	
	/**
	 * Performs HTTP get given a valid URI, returns JSON object representation of response.	
	 * 
	 * @param uri
	 * @return JsonObject
	 * @throws IOException 
	 */
	protected JsonObject executeRequest(){
		InputStream response = null;
		
		try {
			url = uri.toURL();
		} catch (MalformedURLException e1) {
			
		}
				
		try {
			response = url.openStream();
			
			jsonResponse = parseResponseAsJson(response);
			
		} catch (ClientProtocolException e) {
			log.error(e.getMessage());
			
		} catch (IOException e) {
			log.error(e.getMessage());
			log.error(e.getCause());
			log.error(e.getStackTrace().toString());
			log.error("Unknown artist");
			jsonResponse =  null;
		}
		uriBld.clearParameters();

		return jsonResponse;
	}
	
	/**
	 * Builds a string representing response from songkick ready to be parse as JSON.
	 * 
	 * @param  response					HttpRespose received by HttpGet.	
	 * @return StringBuffer				String representing the response. Ready to parse as JSON.
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	protected JsonObject parseResponseAsJson(InputStream response){
		JsonObject jsonResponse = new JsonObject();
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response));
		
		JsonParser jsonParser = new JsonParser();
		
		StringBuffer result = new StringBuffer();
		
		String line = "";
		
		try {
			while ((line = rd.readLine()) != null) {
			    result.append(line);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}

		try {
			jsonResponse = jsonParser.parse(result.toString()).getAsJsonObject();
		} catch(IllegalStateException e){
			jsonResponse.add("resultsPage", jsonParser.parse(result.toString())
					.getAsJsonArray());
		} 
		
		return jsonResponse;
	}
	
	protected boolean isNullResponse(){
		return jsonResponse == null;
	}
}
