package Utils;

import java.io.File;import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import Models.Material;

public class SearchHistorySerializer {

	private static final String SEARCH_HISTORY_FILE_NAME = "search_history.xml";
	private static String filePath;
	
	public SearchHistorySerializer() {
		filePath = Paths.get("").toAbsolutePath().toString() + "\\" + SEARCH_HISTORY_FILE_NAME;
	}

	public void addToHistory(Material material) {
		try {
			File file = new File(filePath);
			JAXBContext jaxbContext = JAXBContext.newInstance(Material.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(material, file);
			jaxbMarshaller.marshal(material, System.out);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public void loadHistory() {
		try {

			File file = new File(filePath);
			JAXBContext jaxbContext = JAXBContext.newInstance(Material.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Material customer = (Material) jaxbUnmarshaller.unmarshal(file);
			System.out.println(customer);

		  } catch (JAXBException e) {
			e.printStackTrace();
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
