package utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TheCatAPI {
	private static final String API_URL = "https://api.thecatapi.com/v1/images/search";
	
	/**
	 * 
	 * @param settings
	 * 		TODO: Implement cat picture options
	 * @return
	 * 		String - The url to a random cat picture
	 * @throws IOException
	 */
	public static String getCat(String settings) throws IOException {
		return getRandomCatJSON(settings).get("url").getAsString();

	}

	private static JsonObject getRandomCatJSON(String settings) throws IOException {
		JsonObject rootobj = (JsonObject) readURL(API_URL).getAsJsonArray().get(0);
		return rootobj;
		
	}
	
	private static JsonElement readURL(String urlString) throws IOException {
		URL url = new URL(API_URL);
		URLConnection request = url.openConnection();
		request.connect();
		JsonParser jp = new JsonParser();
		InputStreamReader ISR = new InputStreamReader(request.getInputStream());
		return jp.parse(ISR);
	}
}
