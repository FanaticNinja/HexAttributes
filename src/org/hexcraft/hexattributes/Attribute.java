package org.hexcraft.hexattributes;

import java.util.List;

import org.hexcraft.hexattributes.api.AttributeBase;
import org.hexcraft.hexattributes.types.*;

public class Attribute {
	
	public List<AttributeBase> register;
	
	public List<Mastery> mastery;
	public List<Deficiency> deficiency;
	public List<Toughness> toughness;
	public List<Weakness> weakness;
	
	public List<PermEffect> permeffect;
	
	public List<Strike> strike;
	public List<Reflect> reflect;
	public List<Immunity> immunity;
	
	public List<Truce> truce;
	
	public List<Food> food;
	
	public List<Passive> passive;
	
	public Attribute() {
		
	}
	
}
