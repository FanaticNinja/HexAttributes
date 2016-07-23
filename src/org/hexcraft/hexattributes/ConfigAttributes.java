package org.hexcraft.hexattributes;

import java.io.File;
import java.util.ArrayList;

import org.hexcraft.HexAttributes;
import org.hexcraft.hexattributes.api.AttributeBase;
import org.hexcraft.hexattributes.types.*;
import org.hexcraft.util.DiscUtil;

public class ConfigAttributes {
	
	private HexAttributes plugin;
	private String configAttributesFolderPath;
	
	public Attribute types;
	
	// -- ConfigTypes
	public Deficiency configDeficiency;
	public Immunity configImmunity;
	public Mastery configMastery;
	public PermEffect configPermEffect;
	public Reflect configReflect;
	public Strike configStrike;
	public Toughness configToughness;
	public Truce configTruce;
	public Weakness configWeakness;
	public Food configFood;
	public Passive configPassive;
	
	// -- load in all configs..
	public ConfigAttributes(HexAttributes plugin) {
		this.plugin = plugin;
		
		configAttributesFolderPath = this.plugin.dataLayerFolderPath + File.separator + "attributes";
		
		// -- init master..
		types = new Attribute();
		// -- init register
		types.register = new ArrayList<AttributeBase>();
		
		types.deficiency = new ArrayList<Deficiency>();
		loadDeficiency();
		
		types.immunity = new ArrayList<Immunity>();
		loadImmunity();
		
		types.mastery = new ArrayList<Mastery>();
		loadMastery();
		
		types.permeffect = new ArrayList<PermEffect>();
		loadPermEffect();
		
		types.reflect = new ArrayList<Reflect>();
		loadReflect();
		
		types.strike = new ArrayList<Strike>();
		loadStrike();
		
		types.toughness = new ArrayList<Toughness>();
		loadToughness();
		
		types.truce = new ArrayList<Truce>();
		loadTruce();
		
		types.weakness = new ArrayList<Weakness>();
		loadWeakness();
		
		types.food = new ArrayList<Food>();
		loadFood();
		
		types.passive = new ArrayList<Passive>();
		loadPassive();
		
	}
	
	private void loadDeficiency() {
		
		String newDirectory = configAttributesFolderPath + File.separator + "deficiency";
		File[] fList = loadAttributes(newDirectory);
		for (File file : fList) {
			
            if (file.isFile()) {
            	String contentAttributes = DiscUtil.readCatch(new File(newDirectory + File.separator + file.getName()));
        		if(contentAttributes != null) {

        			configDeficiency = this.plugin.gson.fromJson(contentAttributes, Deficiency.class);
        			// -- easier config stuff..
        			
        			//plugin.debug("Loading: " + configDeficiency.name + " Cost: " + configDeficiency.cost);
        			types.register.add(new AttributeBase(configDeficiency.name, configDeficiency.cost, configDeficiency.info));
        			types.deficiency.add(configDeficiency);
        		}
            }
        }
	}
	
	private void loadImmunity() {
		
		String newDirectory = configAttributesFolderPath + File.separator + "immunity";
		File[] fList = loadAttributes(newDirectory);
		for (File file : fList) {
            if (file.isFile()) {
            	String contentAttributes = DiscUtil.readCatch(new File(newDirectory + File.separator + file.getName()));
        		if(contentAttributes != null) {
        			configImmunity = this.plugin.gson.fromJson(contentAttributes, Immunity.class);
        			
        			//plugin.debug("Loading: " + configImmunity.name + " Cost: " + configImmunity.cost);
        			
        			// -- easier config stuff..
        			types.register.add(new AttributeBase(configImmunity.name, configImmunity.cost, configImmunity.info));
        			
        			types.immunity.add(configImmunity);
        		}
            }
        }
	}
	
	private void loadMastery() {
		
		String newDirectory = configAttributesFolderPath + File.separator + "mastery";
		File[] fList = loadAttributes(newDirectory);
		for (File file : fList) {
            if (file.isFile()) {
            	String contentAttributes = DiscUtil.readCatch(new File(newDirectory + File.separator + file.getName()));
        		if(contentAttributes != null) {
        			configMastery = this.plugin.gson.fromJson(contentAttributes, Mastery.class);
        			
        			//plugin.debug("Loading: " + configMastery.name + " Cost: " + configMastery.cost);
        			
        			// -- easier config stuff..
        			types.register.add(new AttributeBase(configMastery.name, configMastery.cost, configMastery.info));
        			
        			types.mastery.add(configMastery);
        		}
            }
        }
	}
	
	private void loadPermEffect() {
		
		String newDirectory = configAttributesFolderPath + File.separator + "permeffect";
		File[] fList = loadAttributes(newDirectory);
		for (File file : fList) {
            if (file.isFile()) {
            	String contentAttributes = DiscUtil.readCatch(new File(newDirectory + File.separator + file.getName()));
        		if(contentAttributes != null) {
        			configPermEffect = this.plugin.gson.fromJson(contentAttributes, PermEffect.class);
        			
        			//plugin.debug("Loading: " + configPermEffect.name + " Cost: " + configPermEffect.cost);
        			
        			// -- easier config stuff..
        			types.register.add(new AttributeBase(configPermEffect.name, configPermEffect.cost, configPermEffect.info));
        			
        			types.permeffect.add(configPermEffect);
        		}
            }
        }
	}
	
	private void loadReflect() {
		
		String newDirectory = configAttributesFolderPath + File.separator + "reflect";
		File[] fList = loadAttributes(newDirectory);
		for (File file : fList) {
			
            if (file.isFile()) {
            	String contentAttributes = DiscUtil.readCatch(new File(newDirectory + File.separator + file.getName()));
        		if(contentAttributes != null) {
        			
        			configReflect = this.plugin.gson.fromJson(contentAttributes, Reflect.class);
        			//plugin.debug("Loading: " + configReflect.name + " Cost: " + configReflect.cost);
        			// -- easier config stuff..
        			types.register.add(new AttributeBase(configReflect.name, configReflect.cost, configReflect.info));
        			
        			// -- fix for transient stuff (gson use transeint to ignore stuff!!)
        			configReflect.plugin = plugin;
        			types.reflect.add(configReflect);
        		}
            }
        }
	}
	
	private void loadStrike() {
		
		String newDirectory = configAttributesFolderPath + File.separator + "strike";
		File[] fList = loadAttributes(newDirectory);
		for (File file : fList) {
            if (file.isFile()) {
            	String contentAttributes = DiscUtil.readCatch(new File(newDirectory + File.separator + file.getName()));
        		if(contentAttributes != null) {
        			configStrike = this.plugin.gson.fromJson(contentAttributes, Strike.class);
        			//plugin.debug("Loading: " + configStrike.name + " Cost: " + configStrike.cost);
        			// -- easier config stuff..
        			types.register.add(new AttributeBase(configStrike.name, configStrike.cost, configStrike.info));
        			
        			// -- fix for transient stuff (gson use transeint to ignore stuff!!)
        			configStrike.plugin = plugin;
        			
        			types.strike.add(configStrike);
        		}
            }
        }
	}
	
	private void loadToughness() {
		
		String newDirectory = configAttributesFolderPath + File.separator + "toughness";
		File[] fList = loadAttributes(newDirectory);
		for (File file : fList) {
            if (file.isFile()) {
            	String contentAttributes = DiscUtil.readCatch(new File(newDirectory + File.separator + file.getName()));
        		if(contentAttributes != null) {
        			configToughness = this.plugin.gson.fromJson(contentAttributes, Toughness.class);
        			//plugin.debug("Loading: " + configToughness.name + " Cost: " + configToughness.cost);
        			// -- easier config stuff..
        			types.register.add(new AttributeBase(configToughness.name, configToughness.cost, configToughness.info));
        			
        			types.toughness.add(configToughness);
        		}
            }
        }
	}
	
	private void loadTruce() {
		
		String newDirectory = configAttributesFolderPath + File.separator + "truce";
		File[] fList = loadAttributes(newDirectory);
		for (File file : fList) {
            if (file.isFile()) {
            	String contentAttributes = DiscUtil.readCatch(new File(newDirectory + File.separator + file.getName()));
        		if(contentAttributes != null) {
        			configTruce = this.plugin.gson.fromJson(contentAttributes, Truce.class);
        			//plugin.debug("Loading: " + configTruce.name + " Cost: " + configTruce.cost);
        			// -- easier config stuff..
        			types.register.add(new AttributeBase(configTruce.name, configTruce.cost, configTruce.info));
        			
        			types.truce.add(configTruce);
        		}
            }
        }
	}
	
	private void loadWeakness() {
		
		String newDirectory = configAttributesFolderPath + File.separator + "weakness";
		File[] fList = loadAttributes(newDirectory);
		for (File file : fList) {
            if (file.isFile()) {
            	String contentAttributes = DiscUtil.readCatch(new File(newDirectory + File.separator + file.getName()));
        		if(contentAttributes != null) {
        			configWeakness = this.plugin.gson.fromJson(contentAttributes, Weakness.class);
        			//plugin.debug("Loading: " + configWeakness.name + " Cost: " + configWeakness.cost);
        			// -- easier config stuff..
        			types.register.add(new AttributeBase(configWeakness.name, configWeakness.cost, configWeakness.info));
        			
        			types.weakness.add(configWeakness);
        		}
            }
        }
	}
	
	private void loadFood() {
		
		String newDirectory = configAttributesFolderPath + File.separator + "food";
		File[] fList = loadAttributes(newDirectory);
		for (File file : fList) {
            if (file.isFile()) {
            	String contentAttributes = DiscUtil.readCatch(new File(newDirectory + File.separator + file.getName()));
        		if(contentAttributes != null) {
        			configFood = this.plugin.gson.fromJson(contentAttributes, Food.class);
        			//plugin.debug("Loading: " + configFood.name + " Cost: " + configFood.cost);
        			
        			// -- easier config stuff..
        			types.register.add(new AttributeBase(configFood.name, configFood.cost, configFood.info));
        			
        			types.food.add(configFood);
        		}
            }
        }
	}
	
	private void loadPassive() {
		
		String newDirectory = configAttributesFolderPath + File.separator + "passive";
		File[] fList = loadAttributes(newDirectory);
		for (File file : fList) {
            if (file.isFile()) {
            	String contentAttributes = DiscUtil.readCatch(new File(newDirectory + File.separator + file.getName()));
        		if(contentAttributes != null) {
        			configPassive = this.plugin.gson.fromJson(contentAttributes, Passive.class);
        			//plugin.debug("Loading: " + configPassive.name + " Cost: " + configPassive.cost);
        			// -- easier config stuff..
        			types.register.add(new AttributeBase(configPassive.name, configPassive.cost, configPassive.info));
        			
        			types.passive.add(configPassive);
        		}
            }
        }
	}
	
	public File[] loadAttributes(String directoryName) {
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        
        return fList;
    }
	
	
	
	
	
	
	
	
	

}
