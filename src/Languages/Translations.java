package Languages;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import Startup.AppSettings;

public class Translations {

	public static final String APP_TITLE = "";

	private static Properties translations;

	private static final String DEFAULT_LANGUAGE = "DE";
	private static final List<String> SUPPORTED_LANGUAGES = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("DE");
			add("EN");
		}
	};

	public static String getClientLanguage(String appSettingsLanguage) {
		return SUPPORTED_LANGUAGES.contains(appSettingsLanguage) ? appSettingsLanguage : DEFAULT_LANGUAGE;
	}

	public static void loadTranslations() throws InvalidPropertiesFormatException, IOException {
		translations = new Properties();
		String absolutePath = (Paths.get("").toAbsolutePath().toString() + "\\resources\\" + AppSettings.CLIENT_LANGUAGE.toLowerCase() + ".xml");
			FileInputStream inputStream = new FileInputStream(absolutePath);
			translations.loadFromXML(inputStream);
	}
	
	public static Properties getAll() throws InvalidPropertiesFormatException, IOException {
		if (translations == null) {
			loadTranslations();
		}
		return translations;
	}

	public static String get(String translation) throws InvalidPropertiesFormatException, IOException {
		if (translations == null) {
			loadTranslations();
		}
		return translations.getProperty(translation);
	}

}
