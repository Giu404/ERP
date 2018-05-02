import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Properties;

import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;

public class SAPController {
	
	private HashMap<String, String> dataMap;
	private boolean materialExists;
	
	public SAPController() {
		this.dataMap = new HashMap<String, String>();
		this.materialExists = false;
	}
	
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
    
    public void callFunction(JCoDestination destination, String materialName){
        JCoFunction function;
        JCoStructure structure;
		try {
			function = destination.getRepository().getFunction("BAPI_MATERIAL_GET_DETAIL");
			function.getImportParameterList().setValue("MATERIAL", materialName.toUpperCase());
			function.execute(destination);
			structure = (JCoStructure) function.getExportParameterList().getValue(2);
			//System.out.println(structure.toString());
			
			for(int i = 0; i < structure.getMetaData().getFieldCount(); i++){
	            if(structure.getMetaData().getName(i).equals("MATL_DESC") || structure.getMetaData().getName(i).equals("MATL_TYPE") ||
	            		structure.getMetaData().getName(i).equals("GROSS_WT") || structure.getMetaData().getName(i).equals("UNIT_OF_WT") ||
	            		structure.getMetaData().getName(i).equals("VOLUME") || structure.getMetaData().getName(i).equals("VOLUMEUNIT")){
	            	if(structure.getString(i) == ""){
	            		this.materialExists = false;
	            		return;
	            	} else {
	            		this.dataMap.put(structure.getMetaData().getName(i), structure.getString(i));
	            	}
	            }
	        }
			this.materialExists = true;
            System.out.println(this.dataMap.toString());

	        System.out.println();
			
		} catch (JCoException e) {
			System.out.println(e.toString());
		}
    }
    
    public HashMap<String, String> getDataMap(){
		return this.dataMap;
    	
    }
    
    public boolean getMaterialExistence(){
    	return this.materialExists;
    }
	
}
