package core.utils;

import java.net.URL;

public class ResourceLoader {
	
	public static URL loadResource(String fileName) {
		return ResourceLoader.class.getClassLoader().getResource("MainActivity.fxml");
	}
}
