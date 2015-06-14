package javabandsintown.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import com.google.gson.JsonObject;

public interface HttpConnector {
		
	abstract JsonObject parseResponseAsJson(InputStream response) throws IllegalStateException, IOException;
	
	abstract boolean isEmptyResponse();
	
	abstract JsonObject executeRequest(URI uri);
	
	abstract void buildURI() throws URISyntaxException;
		
	abstract boolean checkResponse();
	
	abstract boolean isNullResponse();
}

