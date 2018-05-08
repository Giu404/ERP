package Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import Models.Material;
import Models.SearchHistory;

public class SearchHistorySerializer {

	private static final String SEARCH_HISTORY_FILE_NAME = "search_history.xml";
	private static String filePath;
	private static SearchHistory searchHistory;
	
	public SearchHistorySerializer() {
		filePath = Paths.get("").toAbsolutePath().toString() + "\\resources\\" + SEARCH_HISTORY_FILE_NAME;
		searchHistory = loadOrCreateHistory();
	}

	public void addToHistory(Material material) {
		searchHistory.add(material);
		try {
			File file = new File(filePath);
			JAXBContext jaxbContext = JAXBContext.newInstance(SearchHistory.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(searchHistory, file);
			jaxbMarshaller.marshal(searchHistory, System.out);
		} catch (JAXBException e) {
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
			JAXBContext jaxbContext = JAXBContext.newInstance(SearchHistory.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			SearchHistory searchHistory = (SearchHistory) jaxbUnmarshaller.unmarshal(file);
			System.out.println("Successfully loaded the search history");
			return searchHistory;

		  } catch (JAXBException e) {
			return new SearchHistory();
		  }

	}

	public void deleteHistory() {
		try {
			Files.deleteIfExists(new File(filePath).toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
