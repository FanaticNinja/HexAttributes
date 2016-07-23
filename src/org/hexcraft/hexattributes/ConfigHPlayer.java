package org.hexcraft.hexattributes;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class ConfigHPlayer {
	
	@SerializedName("slotsMax") public int slotsMax;
	@SerializedName("slotsSpent") public int slotsSpent;
	
	@SerializedName("pointsBase") public int pointsBase;
	@SerializedName("pointsCurrent") public int pointsCurrent;
	
	@SerializedName("assignedAttributes") public List<String> assignedAttributes;

}
