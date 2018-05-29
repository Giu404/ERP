package Models;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;

import Utils.DateTimeUtils;

public class Material {
	
	private static final Map<String, String> validAttributes = Map.ofEntries(
		Map.entry("MATL_DESC", "description"),
		Map.entry("MATL_TYPE", "type"),
		Map.entry("GROSS_WT", "weight"),
		Map.entry("UNIT_OF_WT", "unitOfWeight"),
		Map.entry("VOLUME", "volume"),
		Map.entry("VOLUMEUNIT", "volumeUnit")
	);

	private boolean hasUninitializedAttributes;
	
	private String lookupDateTime;
	private String description;
	private String type;
	private String weight;
	private String unitOfWeight;
	private String volume;
	private String volumeUnit;
	
	public Material() {
		
	}
	
	public Material(LocalDateTime lookupDateTime) {
		this.lookupDateTime = DateTimeUtils.toUtc(lookupDateTime);
		hasUninitializedAttributes = true;
	}
	
	public static Map<String, String> validAttributes() {
		return validAttributes;
	}
	
	public void setValueForAttribute(String value, String sapAttribute) {
		String materialAttributeName = validAttributes.get(sapAttribute);
		try {
			Field field = getClass().getDeclaredField(materialAttributeName);
			field.set(this, value);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
	}
	
	public boolean hasUninitializedAttributes() {
		return hasUninitializedAttributes;
	}
	
	public void setInitialized() {
		hasUninitializedAttributes = false;
	}
	
	//ATTENTION! BEWARE USING THIS METHOD BECAUSE OF DATETIMES!
	public void setLookupDateTime(String lookupDateTime) {
		this.lookupDateTime = lookupDateTime;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setWeight(String weight) {
		this.weight = weight;
	}
	
	public void setUnitOfWeight(String unitOfWeight) {
		this.unitOfWeight = unitOfWeight;
	}
	
	public void setVolume(String volume) {
		this.volume = volume;
	}
	
	public void setVolumeUnit(String volumeUnit) {
		this.volumeUnit = volumeUnit;
	}
	
	//Only for JAXB
	public LocalDateTime getLookupDateTimeUtc() {
		return DateTimeUtils.utcStringToDateTime(lookupDateTime);
	}
	
	public String getLookupDateTime() {
		return lookupDateTime;
	}
	
	//Always use this
	public String getLookupDateTimeLocalizedString() {
		return DateTimeUtils.toLocal(DateTimeUtils.utcStringToDateTime(lookupDateTime));
	}

	public String getDescription() {
		return description;
	}

	public String getType() {
		return type;
	}

	public String getWeight() {
		return weight;
	}

	public String getUnitOfWeight() {
		return unitOfWeight;
	}

	public String getVolume() {
		return volume;
	}

	public String getVolumeUnit() {
		return volumeUnit;
	}
	
	public LocalDateTime localLookupDateTime() {
		return DateTimeUtils.utcStringToDateTime(lookupDateTime);
	}
	
	@Override
	public String toString() {
		return description + " " + " " + type + " " + weight + " " + unitOfWeight + " " + volume + " " +volumeUnit + " " + lookupDateTime;
	}

	public Material mostCurrentDate(Material material) {
		LocalDateTime m1DateTime = this.getLookupDateTimeUtc();
		LocalDateTime m2DateTime = material.getLookupDateTimeUtc();
		return m1DateTime.compareTo(m2DateTime) > 0 ? this : material;
	}
	
}
