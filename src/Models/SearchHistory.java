package Models;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "search_history")
@XmlAccessorType(XmlAccessType.FIELD)
public class SearchHistory {
	
	@XmlElement(name = "material")
	private List<Material> searchHistory = new ArrayList<Material>();
	
	public void setSearchHistory(List<Material> searchHistory) {
		this.searchHistory = searchHistory;
	}
	
	public void add(Material material) {
		searchHistory.add(material);
	}
	
	public List<Material> getSearchHistory() {
		return searchHistory;
	}

}
