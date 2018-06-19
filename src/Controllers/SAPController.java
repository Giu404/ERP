package Controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoStructure;

import Models.Material;
import Startup.AppSettings;
import Utils.ConnectionBuilder;

public class SAPController {

	public JCoDestination tryLogin(String name, String password) {
		Properties properties = ConnectionBuilder.buildConnection(name, password);
		boolean dataFileSuccess = createDestinationDataFile(properties);
		if (!dataFileSuccess) {
			return null;
		}
		try {
			JCoDestination destination = JCoDestinationManager
					.getDestination(AppSettings.getProperty("destinationName"));
			destination.ping();
			return destination;
		} catch (java.lang.Exception e) {
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
		} catch (java.lang.Exception e) {
			return false;
		}
	}

	public Material getMaterialData(JCoDestination destination, String materialName)
			throws InvalidPropertiesFormatException, IOException, InterruptedException, ExecutionException {
		JCoFunction function;
		JCoStructure structure;
		Material material = new Material(LocalDateTime.now());
		Map<String, String> validAttributes = Material.validAttributes();
		try {
			function = destination.getRepository().getFunction("BAPI_MATERIAL_GET_DETAIL");
			function.getImportParameterList().setValue("MATERIAL", materialName.toUpperCase());
			function.execute(destination);
			structure = (JCoStructure) function.getExportParameterList().getValue(2);
			for (int i = 0; i < structure.getMetaData().getFieldCount(); i++) {
				if (validAttributes.containsKey(structure.getMetaData().getName(i))) {
					if (structure.getString(i) == "") {
						return material;
					} else {
						material.setValueForAttribute(structure.getString(i), structure.getMetaData().getName(i));
					}
				}
			}
		} catch (JCoException e) {
			System.out.println(e.toString());
		}
		material.setName(materialName.toUpperCase());
		material.setInitialized();
		return material;
	}

}
