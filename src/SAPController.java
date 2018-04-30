import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;

public class SAPController {
	
	private static final String DESTINATION_NAME1 = "ABAP_AS";
	
	public boolean tryLogin(String name, String password) {
		Properties properties = ConnectionBuilder.BuildConnection(name, password);
		boolean dataFileSuccess = createDestinationDataFile(properties);
		boolean connectionSuccess = false;
		if(dataFileSuccess) {
			connectionSuccess = step1Connect();
		} else {
			return false;
		}
		return connectionSuccess;
	}	

	private boolean createDestinationDataFile(Properties connectProperties) {
		File destCfg = new File(DESTINATION_NAME1 + ".jcoDestination");
		try {
			FileOutputStream fos = new FileOutputStream(destCfg, false);
			connectProperties.store(fos, "for tests only !");
			fos.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean step1Connect()
    {
		try {			
	        JCoDestination destination = JCoDestinationManager.getDestination(DESTINATION_NAME1);
	        System.out.println("Attributes:");
	        System.out.println(destination.getAttributes());
	        System.out.println();
	        return true;
		} catch (Exception e) {
			return false;
		}
    }
	
}
