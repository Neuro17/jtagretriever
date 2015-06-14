package javabandsintown.geonames;

public class GeonamesConfig {
	
	private static final String SCHEME = "http://";
	private static final String HOST = "api.geonames.org";
	private static final String USERNAME = "mastinux";
	private static final String TIMEZONE_JSON = "/timezoneJSON";
	
	public static String getHost() {
		return HOST;
	}

	public static String getUsername() {
		return USERNAME;
	}

	public static String getTimeZoneJson() {
		return TIMEZONE_JSON;
	}

	public static String getScheme() {
		return SCHEME;
	}
}