package org.hexcraft.hexattributes.types;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Food {
	// -- all
	@SerializedName("name") public String name;
	@SerializedName("info") public String info;
	@SerializedName("cost") public int cost;
	
	@SerializedName("foodTypes") public List<String> foodTypes;
	
}
