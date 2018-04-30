import java.util.Properties;

import com.sap.conn.jco.ext.DestinationDataProvider;

public class ConnectionBuilder {
	
	private static final String DEFAULT_LANGUAGE = "DE";
	
	public static Properties BuildConnection(String name, String password) {
		Properties properties = new Properties();
		properties = new Properties();
		properties.setProperty(DestinationDataProvider.JCO_ASHOST, "131.159.9.203");
		properties.setProperty(DestinationDataProvider.JCO_SYSNR, "00");
		properties.setProperty(DestinationDataProvider.JCO_CLIENT, "704");
		properties.setProperty(DestinationDataProvider.JCO_USER, name);
		properties.setProperty(DestinationDataProvider.JCO_PASSWD, password);
		properties.setProperty(DestinationDataProvider.JCO_LANG, DEFAULT_LANGUAGE);
		return properties;
	}
	
	public static Properties BuildConnection(String name, String password, String language) {
		Properties properties = new Properties();
		properties = new Properties();
		properties.setProperty(DestinationDataProvider.JCO_ASHOST, "131.159.9.203");
		properties.setProperty(DestinationDataProvider.JCO_SYSNR, "00");
		properties.setProperty(DestinationDataProvider.JCO_CLIENT, "704");
		properties.setProperty(DestinationDataProvider.JCO_USER, name);
		properties.setProperty(DestinationDataProvider.JCO_PASSWD, password);
		properties.setProperty(DestinationDataProvider.JCO_LANG, language);
		return properties;
	}

}
