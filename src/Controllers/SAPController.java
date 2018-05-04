package Controllers;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoStructure;

import Startup.AppSettings;
import Startup.ConnectionBuilder;

public class SAPController {
	
	private HashMap<String, String> dataMap;
	private static final List<String> displayData = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
	{
		add("MATL_DESC");
		add("MATL_TYPE");
		add("GROSS_WT");
		add("UNIT_OF_WT");
		add("VOLUME");
		add("VOLUMEUNIT");
	}};
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
			connectProperties.store(fos, "");
			fos.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
    
    public void getMaterialData(JCoDestination destination, String materialName){
        JCoFunction function;
        JCoStructure structure;
		try {	
			function = destination.getRepository().getFunction("BAPI_MATERIAL_GET_DETAIL");
			function.getImportParameterList().setValue("MATERIAL", materialName.toUpperCase());
			function.execute(destination);
			structure = (JCoStructure) function.getExportParameterList().getValue(2);			
			for(int i = 0; i < structure.getMetaData().getFieldCount(); i++){
	            if(displayData.contains(structure.getMetaData().getName(i))) {
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
