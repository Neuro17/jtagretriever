package javabandsintown.config;

public class BandsintownConfig {
	private static final String SCHEME = "http";
	
	private static final String HOST = "api.bandsintown.com";
	
	private static final String ARTIST_PATH = "/artists";
	
	private static final String EVENT_PATH = "/events";
	
	private static final String VENUE_PATH = "/venues";

	private static String apiKey = "YOUR_APP_ID";
	
	private static String apiVersion = "2.0";

	public static String getApiVersion() {
		return apiVersion;
	}

	public static void setApiVersion(String apiVersion) {
		BandsintownConfig.apiVersion = apiVersion;
	}

	public static String getScheme() {
		return SCHEME;
	}

	public static String getHost() {
		return HOST;
	}

	public static String getArtistPath() {
		return ARTIST_PATH;
	}

	public static String getEventPath() {
		return EVENT_PATH;
	}

	public static String getVenuePath() {
		return VENUE_PATH;
	}

	public static String getApiKey() {
		return apiKey;
	}

	public static void setApiKey(String apiKey) {
		BandsintownConfig.apiKey = apiKey;
	}
	
	
}
