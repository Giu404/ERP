package Utils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Languages.Language;

public class DateTimeUtils {

	public static LocalDateTime utcStringToDateTime(String dateTime) {
		return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(Language.UTC_DATE_TIME_FORMATATION));
	}
	
	public static String toUtc(LocalDateTime dateTime) {
		return dateTime.format(DateTimeFormatter.ofPattern(Language.UTC_DATE_TIME_FORMATATION));
	}
	
	public static String toLocal(LocalDateTime dateTime) {
		try {
			return dateTime.format(DateTimeFormatter.ofPattern(Language.getDatTimeFormatation()));
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	
}
