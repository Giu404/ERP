package Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Models.Material;
import Models.SearchHistory;
import Startup.AppSettings;

public class SearchHistorySerializer {

	private static final String SEARCH_HISTORY_FILE_NAME = "search_history.json";
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
			SearchHistory.sortByDateTime(searchHistory.getSearchHistory());
			if(searchHistory.getSearchHistory().size() > searchHistoryMaxSize) {
				searchHistory.getSearchHistory().subList(0, searchHistory.getSearchHistory().size() - searchHistoryMaxSize).clear();
			}			
			try (Writer writer = new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8)) {
			    Gson gson = new GsonBuilder().setPrettyPrinting().create();
			    gson.toJson(searchHistory, writer);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
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
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			InputStream inputStream = new FileInputStream(file);
			Reader reader = new InputStreamReader(inputStream, "UTF-8");
			searchHistory = gson.fromJson(reader, SearchHistory.class);
			if(searchHistory.getSearchHistory().size() > searchHistoryMaxSize) {
				SearchHistory.sortByDateTime(searchHistory.getSearchHistory());
				searchHistory.getSearchHistory().subList(0, searchHistory.getSearchHistory().size() - searchHistoryMaxSize).clear();
			}
			return searchHistory;

		  } catch (Exception e) {
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
				materials.replace(material.getDescription(), z);
			}
		}
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
