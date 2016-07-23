package org.hexcraft.hexattributes.types;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Truce {
	// -- all
	@SerializedName("name") public String name;
	@SerializedName("info") public String info;
	@SerializedName("cost") public int cost;
		
	@SerializedName("entities") public List<String> entities;
	@SerializedName("brokenTimout") public int brokenTimout;
	
	/*
	public Truce(String name, String info, int cost, List<String> entities, int brokenTimout) {
		this.name = name;
		this.info = info;
		this.cost = cost;
		this.entities = entities;
		this.brokenTimout = brokenTimout;
	}
	*/
	
}
