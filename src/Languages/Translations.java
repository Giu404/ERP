package Languages;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Startup.AppSettings;

public class Translations {

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
		return SUPPORTED_LANGUAGES.contains(appSettingsLanguage.toUpperCase()) ? appSettingsLanguage.toUpperCase() : DEFAULT_LANGUAGE;
	}

	public static void loadTranslations() throws InvalidPropertiesFormatException, IOException {
		Gson gson = new GsonBuilder().create();
		String absolutePath = (Paths.get("").toAbsolutePath().toString() + "\\resources\\" + AppSettings.CLIENT_LANGUAGE.toLowerCase() + ".json");
		InputStream inputStream = new FileInputStream(absolutePath);
		Reader reader = new InputStreamReader(inputStream, "UTF-8");
		translations = gson.fromJson(reader, Properties.class);
		System.out.println(translations.toString());
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
