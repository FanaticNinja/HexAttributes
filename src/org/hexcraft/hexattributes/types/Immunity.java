package org.hexcraft.hexattributes.types;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Immunity {
	
	// -- all
	@SerializedName("name") public String name;
	@SerializedName("info") public String info;
	@SerializedName("cost") public int cost;
	
	@SerializedName("immunityStrikes") public List<String> immunityStrikes;
	@SerializedName("immunityReflects") public List<String> immunityReflects;
	@SerializedName("immunityDamageCause") public List<String> immunityDamageCause;
	@SerializedName("immunityPotionTypes") public List<String> immunityPotionTypes;
	
	/*
	public Immunity(String name, String info, int cost, List<String> immunityStrikes, List<String> immunityReflects, List<String> immunityDamageCause, List<String> immunityPotionTypes) {
		this.name = name;
		this.info = info;
		this.cost = cost;
		this.immunityStrikes = immunityStrikes;
		this.immunityReflects = immunityReflects;
		this.immunityDamageCause = immunityDamageCause;
		this.immunityPotionTypes = immunityPotionTypes;
	}
	*/
	
	
}