import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;

public class SAPController {
	
	private static final String DESTINATION_NAME1 = "ABAP_AS";
	
	public JCoDestination tryLogin(String name, String password) {
		Properties properties = ConnectionBuilder.BuildConnection(name, password);
		boolean dataFileSuccess = createDestinationDataFile(properties);
		if(!dataFileSuccess){
			return null;
		}
		try {			
	        JCoDestination destination = JCoDestinationManager.getDestination(DESTINATION_NAME1);
	        System.out.println("Attributes:");
	        System.out.println(destination.getAttributes());
	        System.out.println();
	        return destination;
		} catch (Exception e) {
			return null;
		}
		
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
	
    public static void step3SimpleCall(JCoDestination destination) throws JCoException {
        JCoFunction function = destination.getRepository().getFunction("BAPI_MATERIAL_EXISTENCECHECK");
        if(function == null)
            throw new RuntimeException("STFC_CONNECTION not found in SAP.");
 
        //function.getImportParameterList().setValue("REQUTEXT", "Hello SAP");
        
        try
        {
            function.execute(destination);
            System.out.println(function.getName());
        }
        catch(AbapException e)
        {
            System.out.println(e.toString());
            return;
        }
       
        System.out.println("STFC_CONNECTION finished:");
        System.out.println(" Echo: " + function.getExportParameterList().getString("ECHOTEXT"));
        System.out.println(" Response: " + function.getExportParameterList().getString("RESPTEXT"));
        System.out.println();
    }
	
}
