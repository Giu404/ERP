package Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchHistory {
	
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

	public static void sortByDateTime(List<Material> list) {
		Collections.sort(list, new Comparator<Material>() {
	        @Override
	        public int compare(Material m1, Material m2) {
	        	return m1.localLookupDateTime().compareTo(m2.localLookupDateTime());
	        }
	    });
	}	
}
