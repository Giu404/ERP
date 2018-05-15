package Startup;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class AppSettings {
	
	private static Properties appSettings;
	private static final String APP_SETTINGS_FILE_NAME = "app.config";
	public static String CLIENT_LANGUAGE = "DE";
	
	public static void loadAppSettings() {
		appSettings = new Properties();
		Gson gson = new GsonBuilder().create();
		String absolutePath = (Paths.get("").toAbsolutePath().getParent().toAbsolutePath().toString() + "\\" + APP_SETTINGS_FILE_NAME);
		try {
			InputStream inputStream = new FileInputStream(absolutePath);
			Reader reader = new InputStreamReader(inputStream, "UTF-8");
			Map<String, String> jsonMap = gson.fromJson(reader, new TypeToken<Map<String, String>>(){}.getType());
			appSettings.putAll(jsonMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		CLIENT_LANGUAGE = appSettings.getProperty("clientLanguage");
	}
	
	public static Properties getAppSettings() {
		return appSettings;
	}
	
	public static String getProperty(String key) {
		return appSettings.getProperty(key);
	}

}
