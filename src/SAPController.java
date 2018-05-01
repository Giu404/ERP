import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;

public class SAPController {
	
	public JCoDestination tryLogin(String name, String password) {
		Properties properties = ConnectionBuilder.buildConnection(name, password);
		boolean dataFileSuccess = createDestinationDataFile(properties);
		if(!dataFileSuccess){
			return null;
		}
		try {			
	        JCoDestination destination = JCoDestinationManager.getDestination(AppSettings.getProperty("destinationName"));
	        System.out.println("Attributes:");
	        System.out.println(destination.getAttributes());
	        System.out.println();
	        return destination;
		} catch (Exception e) {
			return null;
		}
		
	}	

	private boolean createDestinationDataFile(Properties connectProperties) {
		File destCfg = new File(AppSettings.getProperty("destinationName") + ".jcoDestination");
		try {
			FileOutputStream fos = new FileOutputStream(destCfg, false);
			connectProperties.store(fos, "for tests only !");
			fos.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
    public void callFunction(JCoDestination destination) throws JCoException {
        JCoFunction function = destination.getRepository().getFunction("BAPI_MATERIAL_GETLIST");
        if(function == null)
            throw new RuntimeException("BAPI_MATERIAL_GETLIST not found in SAP.");
 
        System.out.println(function.getImportParameterList().toString());
        
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
       
        System.out.println("BAPI_MATERIAL_GETLIST finished:");
        //System.out.println(function.getExportParameterList().toString());
        //System.out.println(function.getTableParameterList().toString());
        System.out.println();
        
    }
    
    public void workWithTable(JCoDestination destination) throws JCoException{
        JCoFunction function = destination.getRepository().getFunction("BAPI_MATERIAL_GETLIST");
        if(function == null)
            throw new RuntimeException("BAPI_MATERIAL_GETLIST not found in SAP.");

        try
        {
            function.execute(destination);
        }
        catch(AbapException e)
        {
            System.out.println(e.toString());
            return;
        }
        
        System.out.println(function.toString());
        
        JCoTable table3 = function.getTableParameterList().getTable(3);
        System.out.println(table3.toString());
        
        for (int i = 0; i < table3.getNumRows(); i++) 
        {
            table3.setRow(i);
            System.out.println(table3.getString("MATERIAL") + '\t' + table3.getString("MATL_DESC"));
        }
        
        function = destination.getRepository().getFunction("BAPI_MATERIAL_GET_DETAIL");
        function.getImportParameterList().setValue("MATERIAL", "METALLROHR M");
        //System.out.println(function.toString());
        function.execute(destination);
        System.out.println(function.getExportParameterList().getValue(2));
        
        // TODO: DIESER TEIL BRINGT UNS UNSER MATERIAL
    }
	
}
