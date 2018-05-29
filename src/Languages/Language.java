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

public class Language {

	private static Properties translations;

	private static final String DEFAULT_LANGUAGE = "DE";
	private static final List<String> SUPPORTED_LANGUAGES = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("DE");
			add("EN");
		}
	};
	public static final String UTC_DATE_TIME_FORMATATION = "yyyy-MM-dd HH:mm:ss";
	private static final String DEFAULT_DATE_TIME_FORMATATION = "dd.MM.yyyy HH:mm:ss";
	private static String dateTimeFormatation;
	
	public static String getClientLanguage(String appSettingsLanguage) {
		return SUPPORTED_LANGUAGES.contains(appSettingsLanguage.toUpperCase()) ? appSettingsLanguage.toUpperCase() : DEFAULT_LANGUAGE;
	}

	public static void loadResources() throws InvalidPropertiesFormatException, IOException {
		Gson gson = new GsonBuilder().create();
		String absolutePath = (Paths.get("").toAbsolutePath().toString() + "\\resources\\" + AppSettings.CLIENT_LANGUAGE.toLowerCase() + ".json");
		InputStream inputStream = new FileInputStream(absolutePath);
		Reader reader = new InputStreamReader(inputStream, "UTF-8");
		translations = gson.fromJson(reader, Properties.class);
		System.out.println(translations.toString());
	}
	
	public static Properties getAllResources() throws InvalidPropertiesFormatException, IOException {
		if (translations == null) {
			loadResources();
		}
		return translations;
	}

	public static String get(String translation) throws InvalidPropertiesFormatException, IOException {
		if (translations == null) {
			loadResources();
		}
		return translations.getProperty(translation);
	}
	
	public static String getDatTimeFormatation() throws InvalidPropertiesFormatException, IOException {
		if(dateTimeFormatation == null) {
			setDateTimeFormatation();
		}
		return dateTimeFormatation != null ? dateTimeFormatation : DEFAULT_DATE_TIME_FORMATATION;
	}
	
	private static void setDateTimeFormatation() throws InvalidPropertiesFormatException, IOException {
		if (translations == null) {
			loadResources();
		}
		dateTimeFormatation = translations.getProperty("dateTimeFormatation") != null ? translations.getProperty("dateTimeFormatation") : DEFAULT_DATE_TIME_FORMATATION;
	}

}
