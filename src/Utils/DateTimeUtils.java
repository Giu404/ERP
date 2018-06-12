package Utils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Languages.Language;

public class DateTimeUtils {
	
	//TODO: Didn't think about time zones yet... Should be implemented soon!

	public static LocalDateTime utcStringToDateTime(String dateTime) {
		return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(Language.UTC_DATE_TIME_FORMATATION));
	}
	
	public static String toUtc(LocalDateTime dateTime) {
		return dateTime.format(DateTimeFormatter.ofPattern(Language.UTC_DATE_TIME_FORMATATION));
	}
	
	public static String toLocal(LocalDateTime dateTime) {
		try {
			return dateTime.format(DateTimeFormatter.ofPattern(Language.getDateTimeFormatation()));
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static LocalDateTime stringToUtcDateTime(String dateTime) {
		try {
			return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(Language.getDateTimeFormatation()));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
