package org.hexcraft.hexattributes.types;

import com.google.gson.annotations.SerializedName;

public class PermEffect {
	// -- all
	@SerializedName("name") public String name;
	@SerializedName("info") public String info;
	@SerializedName("cost") public int cost;
	
	@SerializedName("effect") public String effect;
	@SerializedName("duration") public int duration = 1200;
	@SerializedName("amplifier") public int amplifier = 0;
	
	/*
	public PermEffect(String name, String info, int cost, String effect, int duration, int amplifier) {
		this.name = name;
		this.info = info;
		this.cost = cost;
		this.effect = effect;
		this.duration = duration;
		this.amplifier = amplifier;
	}
	*/
}
