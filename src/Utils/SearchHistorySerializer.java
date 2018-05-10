package Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import Models.Material;
import Models.SearchHistory;
import Startup.AppSettings;

public class SearchHistorySerializer {

	private static final String SEARCH_HISTORY_FILE_NAME = "search_history.xml";
	private static String filePath;
	private static SearchHistory searchHistory;
	private static int searchHistoryMaxSize = 30;
	
	public SearchHistorySerializer() {
		filePath = Paths.get("").toAbsolutePath().toString() + "\\resources\\" + SEARCH_HISTORY_FILE_NAME;
		try {
			searchHistoryMaxSize = Integer.parseInt(AppSettings.getProperty("searchHistoryMaxSize"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		searchHistory = loadOrCreateHistory();
	}

	//Check how big the history already is
	public void addToHistory(Material material) {
		searchHistory.add(material);
		try {
			File file = new File(filePath);
			JAXBContext jaxbContext = JAXBContext.newInstance(SearchHistory.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			SearchHistory.sortByDateTime(searchHistory.getSearchHistory());
			if(searchHistory.getSearchHistory().size() > searchHistoryMaxSize) {
				searchHistory.getSearchHistory().subList(0, searchHistory.getSearchHistory().size() - searchHistoryMaxSize).clear();
			}
			jaxbMarshaller.marshal(searchHistory, file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	//TODO: Maybe the file is just too big to read. Consider handling this.
	@SuppressWarnings("finally")
	public SearchHistory loadOrCreateHistory() {
		File file = new File(filePath);	
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				return new SearchHistory();
			}
		}
		try {			
			JAXBContext jaxbContext = JAXBContext.newInstance(SearchHistory.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			SearchHistory searchHistory = (SearchHistory) jaxbUnmarshaller.unmarshal(file);
			System.out.println("Successfully loaded the search history");
			//Not sure if the if statement is actually necessary... The subList method might work with smaller lists than the indices you provide
			if(searchHistory.getSearchHistory().size() > searchHistoryMaxSize) {
				SearchHistory.sortByDateTime(searchHistory.getSearchHistory());
				searchHistory.getSearchHistory().subList(0, searchHistory.getSearchHistory().size() - searchHistoryMaxSize).clear();
			}
			return searchHistory;

		  } catch (JAXBException e) {
			return new SearchHistory();
		  }
	}
	
	public Map<String, Material> getAccumulatedMaterialList() {
		Map<String, Material> materials = new HashMap<String, Material>();
		for(Material material : searchHistory.getSearchHistory()) {
			if(!materials.containsKey(material.getDescription())) {
				materials.put(material.getDescription(), material);
			} else {
				Material z = material.mostCurrentDate(materials.get(material.getDescription()));
		//		String x = materials.get(material.getDescription());
		//		LocalDateTime cur = DateTimeUtils.stringToUtcDateTime(x);
		//		if(cur.compareTo(DateTimeUtils.stringToUtcDateTime(material.getLocalLookupDateTime())) > 0) {
			//		materials.replace(material.getDescription(), DateTimeUtils.toLocal(cur));
				//}
				materials.replace(material.getDescription(), z);
			}
		}
		System.out.println(materials.toString());
		return materials;
	}

	public void deleteHistory() {
		try {
			Files.deleteIfExists(new File(filePath).toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
