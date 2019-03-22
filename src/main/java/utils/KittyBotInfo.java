package utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.filefilter.RegexFileFilter;

public class KittyBotInfo {
	public static final String VERSION_REGEX = "kittyBot-([0-9]*\\.[0-9]*\\.[0-9]*_*[0-9]*)\\.jar";
	public static final FilenameFilter FILE_REGEX = new RegexFileFilter(VERSION_REGEX);
	
	public String CURRENT_VERSION;
	
	public void setCURRENT_VERSION(String ver) {
		CURRENT_VERSION = ver;
	}
	
	public String getCurrentVersion() {
		return CURRENT_VERSION;
	}
	
	/**
	 * Searches the target folder for the newest version number of kittybot.
	 * @return
	 * 		the newest version number.
	 */
	public String findNewVersion() {
		System.out.println();
		File dir;
		try {
			dir = new File(getTargetFolderPath());
		} catch (MalformedURLException | URISyntaxException e) {
			ExceptionHandler.printErr(e);
			return "N/A";
		}
		String[] matches = dir.list(FILE_REGEX);
		
		if (matches.length >= 1) {
			Pattern versionPattern = Pattern.compile(VERSION_REGEX);
			Matcher versionMatcher = versionPattern.matcher(matches[matches.length - 1]);
			if(versionMatcher.find()) {
				return versionMatcher.group(1);
			} else {
				ExceptionHandler.printErr( new IOException("Version not found"));
			}
		} else {
			ExceptionHandler.printErr( new IOException("Jar not found"));
		}
		
		return "N/A";
	}
	
	public String getCurrentJarPath() throws URISyntaxException, IOException {
		return getJarPath(getCurrentVersion());
	}
	
	public String getNewestJarPath() throws URISyntaxException, IOException {
		return getJarPath(findNewVersion());
	}
	
	public String getJarPath(String version) throws URISyntaxException, IOException {
		File dir = new File(getTargetFolderPath());
		return dir.getAbsolutePath() + getJarName(version);
		
	}
	private String getTargetFolderPath() throws URISyntaxException, MalformedURLException {
		return Paths.get("target").toUri().toURL().toString().replace("file:/", "");
	}
	
	/**
	 * 
	 * @param version
	 * @return
	 * 		The name of the jar for the given version ("\kittyBot-{VERSION}.jar")
	 */
	private static String getJarName(String version) {
		return "\\kittyBot-" + version + ".jar";
	}
	
}
