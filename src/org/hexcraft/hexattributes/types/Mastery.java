package org.hexcraft.hexattributes.types;

import com.google.gson.annotations.SerializedName;


public class Mastery {
	// -- all
	@SerializedName("name") public String name;
	@SerializedName("info") public String info;
	@SerializedName("cost") public int cost;
	
	@SerializedName("damage") public double damage = 0.25;
	
	public Mastery(String name, String info, int cost, double damage) {
		this.name = name;
		this.info = info;
		this.cost = cost;
		this.damage = damage;
	}
	
}
