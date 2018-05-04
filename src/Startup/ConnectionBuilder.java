package Startup;
import java.util.Properties;

import com.sap.conn.jco.ext.DestinationDataProvider;

public class ConnectionBuilder {
	
	public static Properties buildConnection(String name, String password) {
		Properties properties = new Properties();
		properties = new Properties();
		properties.setProperty(DestinationDataProvider.JCO_ASHOST, AppSettings.getProperty("jco_ashost"));
		properties.setProperty(DestinationDataProvider.JCO_SYSNR, AppSettings.getProperty("jco_sysnr"));
		properties.setProperty(DestinationDataProvider.JCO_CLIENT, AppSettings.getProperty("jco_client"));
		properties.setProperty(DestinationDataProvider.JCO_USER, name);
		properties.setProperty(DestinationDataProvider.JCO_PASSWD, password);
		properties.setProperty(DestinationDataProvider.JCO_LANG, AppSettings.getProperty("clientLanguage"));
		return properties;
	}
	
	public static Properties buildConnectionMockLogin() {
		Properties properties = new Properties();
		properties = new Properties();
		properties.setProperty(DestinationDataProvider.JCO_ASHOST, AppSettings.getProperty("jco_ashost"));
		properties.setProperty(DestinationDataProvider.JCO_SYSNR, AppSettings.getProperty("jco_sysnr"));
		properties.setProperty(DestinationDataProvider.JCO_CLIENT, AppSettings.getProperty("jco_client"));
		properties.setProperty(DestinationDataProvider.JCO_USER, AppSettings.getProperty("username"));
		properties.setProperty(DestinationDataProvider.JCO_PASSWD, AppSettings.getProperty("password"));
		properties.setProperty(DestinationDataProvider.JCO_LANG, AppSettings.getProperty("clientLanguage"));
		return properties;
	}

}
