package Startup;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import com.google.gson.*;

import Languages.Language;

public class AppSettings {
	
	private static Properties appSettings;
	private static final String APP_SETTINGS_FILE_NAME = "app.config";
	public static String CLIENT_LANGUAGE = "DE";
	public static String absolutePath;
	
	public static void loadAppSettings() {
		Gson gson = new GsonBuilder().create();
		absolutePath = (Paths.get("").toAbsolutePath().getParent().toAbsolutePath().toString() + "\\" + APP_SETTINGS_FILE_NAME);
		try {
			InputStream inputStream = new FileInputStream(absolutePath);
			Reader reader = new InputStreamReader(inputStream, "UTF-8");
			appSettings = gson.fromJson(reader, Properties.class);
			CLIENT_LANGUAGE = appSettings.getProperty("clientLanguage");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void setStayLoggedIn(boolean stayLoggedIn) throws FileNotFoundException, IOException {
		appSettings.replace("stay_logged_in", stayLoggedIn);
		updateAppSettingsFile();
	}
	
	public static void setClientLanguage(String language) throws InvalidPropertiesFormatException, IOException {
		if(Language.isLanguageSupported(language)) {
			CLIENT_LANGUAGE = language;
			appSettings.replace("clientLanguage", language);
			Language.loadResources();
			updateAppSettingsFile();
		}
	}
	
	public static void updateAppSettingsFile() throws FileNotFoundException, IOException {
		try (Writer writer = new OutputStreamWriter(new FileOutputStream(absolutePath), StandardCharsets.UTF_8)) {
		    Gson gson = new GsonBuilder().create();
		    gson.toJson(appSettings, writer);
		}
	}
	
	public static Properties getAppSettings() {
		return appSettings;
	}
	
	public static String getProperty(String key) {
		return appSettings.getProperty(key);
	}

}
