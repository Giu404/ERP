package Startup;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.Properties;

public class AppSettings {
	
	private static Properties appSettings;
	private static final String APP_SETTINGS_FILE_NAME = "app.config";
	public static String CLIENT_LANGUAGE = "DE";
	
	public static void loadAppSettings() {
		appSettings = new Properties();
		String absolutePath = (Paths.get("").toAbsolutePath().getParent().toAbsolutePath().toString() + "\\" + APP_SETTINGS_FILE_NAME);
		try {
			FileInputStream inputStream = new FileInputStream(absolutePath);
			appSettings.loadFromXML(inputStream);
			CLIENT_LANGUAGE = appSettings.getProperty("clientLanguage");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Properties getAppSettings() {
		return appSettings;
	}
	
	public static String getProperty(String key) {
		return appSettings.getProperty(key);
	}

}
